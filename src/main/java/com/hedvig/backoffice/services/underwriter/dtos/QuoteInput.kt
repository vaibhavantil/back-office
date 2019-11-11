package com.hedvig.backoffice.services.underwriter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.hedvig.backoffice.graphql.types.QuoteInput
import java.util.UUID

data class QuoteInputDto(
  val productType: ProductType?,
  val incompleteHouseQuoteData: QuoteInputDataDto.HouseQuoteInputDto?,
  val incompleteApartmentQuoteData: QuoteInputDataDto.ApartmentQuoteInputDto?,
  val originatingProductId: UUID?,
  val currentInsurer: String? = null
) {
  companion object {
    @JvmStatic
    fun from(quoteInput: QuoteInput): QuoteInputDto =
      QuoteInputDto(
        productType = quoteInput.productType?.toString()?.let(ProductType::valueOf),
        currentInsurer = quoteInput.currentInsurer,
        originatingProductId = quoteInput.originatingProductId,
        incompleteHouseQuoteData = quoteInput.houseData
          ?.let {
            QuoteInputDataDto.HouseQuoteInputDto(
              street = quoteInput.houseData.street,
              zipCode = quoteInput.houseData.zipCode,
              city = quoteInput.houseData.city,
              livingSpace = quoteInput.houseData.livingSpace,
              householdSize = quoteInput.houseData.householdSize,
              extraBuildings = quoteInput.houseData.extraBuildings?.map((ExtraBuilding)::from),
              ancillaryArea = quoteInput.houseData.ancillaryArea,
              numberOfBathrooms = quoteInput.houseData.numberOfBathrooms,
              yearOfConstruction = quoteInput.houseData.yearOfConstruction,
              isSubleted = quoteInput.houseData.isSubleted
            )
          },
        incompleteApartmentQuoteData = quoteInput.apartmentData
          ?.let {
            QuoteInputDataDto.ApartmentQuoteInputDto(
              street = quoteInput.apartmentData.street,
              zipCode = quoteInput.apartmentData.zipCode,
              city = quoteInput.apartmentData.city,
              livingSpace = quoteInput.apartmentData.livingSpace,
              householdSize = quoteInput.apartmentData.householdSize,
              subType = quoteInput.apartmentData.subType
            )
          }
      )
  }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(value = QuoteData.ApartmentQuoteData::class, name = "apartment"),
  JsonSubTypes.Type(value = QuoteData.HouseQuoteData::class, name = "house")
)
sealed class QuoteInputDataDto {
  data class ApartmentQuoteInputDto(
    val street: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val householdSize: Int? = null,
    val livingSpace: Int? = null,

    val subType: com.hedvig.backoffice.services.product_pricing.dto.ProductType? = null
  ) : QuoteInputDataDto()

  data class HouseQuoteInputDto(
    val street: String?,
    val city: String?,
    val zipCode: String?,
    val householdSize: Int?,
    val livingSpace: Int?,
    val ancillaryArea: Int? = null,
    val yearOfConstruction: Int? = null,
    val numberOfBathrooms: Int? = null,
    val extraBuildings: List<ExtraBuilding>? = null,
    val isSubleted: Boolean? = null
  ) : QuoteInputDataDto()
}

