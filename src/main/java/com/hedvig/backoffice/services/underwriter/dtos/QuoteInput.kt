package com.hedvig.backoffice.services.underwriter.dtos

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.hedvig.backoffice.graphql.types.QuoteInput
import com.hedvig.backoffice.services.product_pricing.dto.contract.ExtraBuilding
import java.util.UUID

data class QuoteInputDto(
  val productType: ProductType?,
  val incompleteHouseQuoteData: QuoteInputDataDto.HouseQuoteInputDto?,
  val incompleteApartmentQuoteData: QuoteInputDataDto.ApartmentQuoteInputDto?,
  val norwegianHomeContentQuoteData: QuoteInputDataDto.NorwegianHomeContentQuoteInputDto?,
  val norwegianTravelQuoteData: QuoteInputDataDto.NorwegianTravelQuoteInputDto?,
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
              extraBuildings = quoteInput.houseData.extraBuildings,
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
          },
        norwegianHomeContentQuoteData = quoteInput.norwegianHomeContentData
          ?.let {
            QuoteInputDataDto.NorwegianHomeContentQuoteInputDto(
              street = quoteInput.norwegianHomeContentData.street,
              zipCode = quoteInput.norwegianHomeContentData.zipCode,
              city = quoteInput.norwegianHomeContentData.city,
              livingSpace = quoteInput.norwegianHomeContentData.livingSpace,
              coInsured = quoteInput.norwegianHomeContentData.householdSize?.minus(1),
              type = quoteInput.norwegianHomeContentData.type
            )
          },
        norwegianTravelQuoteData = quoteInput.norwegianTravelData
          ?.let {
            QuoteInputDataDto.NorwegianTravelQuoteInputDto(
              coInsured = quoteInput.norwegianTravelData.householdSize?.minus(1)
            )
          }
      )
  }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(value = QuoteInputDataDto.ApartmentQuoteInputDto::class, name = "apartment"),
  JsonSubTypes.Type(value = QuoteInputDataDto.HouseQuoteInputDto::class, name = "house"),
  JsonSubTypes.Type(value = QuoteInputDataDto.NorwegianHomeContentQuoteInputDto::class, name = "norwegianHomeContent"),
  JsonSubTypes.Type(value = QuoteInputDataDto.NorwegianTravelQuoteInputDto::class, name = "norwegianTravel")
)
sealed class QuoteInputDataDto {
  data class ApartmentQuoteInputDto(
    val street: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val householdSize: Int? = null,
    val livingSpace: Int? = null,

    val subType: SwedishApartmentType? = null
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

  data class NorwegianHomeContentQuoteInputDto(
    val street: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val coInsured: Int? = null,
    val livingSpace: Int? = null,

    val type: NorwegianHomeContentType? = null
  ) : QuoteInputDataDto()

  data class NorwegianTravelQuoteInputDto(
    val coInsured: Int? = null
  ) : QuoteInputDataDto()
}
