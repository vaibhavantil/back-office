package com.hedvig.backoffice.graphql.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.hedvig.backoffice.graphql.UnionType
import com.hedvig.backoffice.services.product_pricing.dto.contract.ExtraBuilding
import com.hedvig.backoffice.services.underwriter.dtos.QuoteDto
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.UUID


enum class ProductType {
  APARTMENT,
  HOUSE,
  OBJECT,
  UNKNOWN
}

enum class QuoteState {
  INCOMPLETE,
  QUOTED,
  SIGNED,
  EXPIRED
}

data class Quote(
  val id: UUID,
  val createdAt: Instant,
  val price: BigDecimal? = null,
  val productType: ProductType,
  val state: QuoteState,
  val initiatedFrom: String,
  val attributedTo: String,
  val data: QuoteData,
  val currentInsurer: String? = null,
  val startDate: LocalDate? = null,
  val validity: Long,
  val memberId: String? = null,
  val breachedUnderwritingGuidelines: List<String>?,
  val isComplete: Boolean,
  val signedProductId: UUID?,
  val originatingProductId: UUID?
) {
  companion object {
    @JvmStatic
    fun from(quote: QuoteDto): Quote =
      Quote(
        id = quote.id,
        currentInsurer = quote.currentInsurer,
        memberId = quote.memberId,
        startDate = quote.startDate,
        price = quote.price,
        data = when {
          quote.data is com.hedvig.backoffice.services.underwriter.dtos.QuoteData.ApartmentQuoteData -> QuoteData.ApartmentQuoteData(
            id = quote.data.id!!,
            ssn = quote.data.ssn,
            firstName = quote.data.firstName,
            lastName = quote.data.lastName,
            street = quote.data.street,
            zipCode = quote.data.zipCode,
            city = quote.data.city,
            livingSpace = quote.data.livingSpace,
            householdSize = quote.data.householdSize,
            subType = quote.data.subType
          )
          quote.data is com.hedvig.backoffice.services.underwriter.dtos.QuoteData.HouseQuoteData -> QuoteData.HouseQuoteData(
            id = quote.data.id!!,
            ssn = quote.data.ssn,
            firstName = quote.data.firstName,
            lastName = quote.data.lastName,
            street = quote.data.street,
            zipCode = quote.data.zipCode,
            city = quote.data.city,
            livingSpace = quote.data.livingSpace,
            householdSize = quote.data.householdSize,
            numberOfBathrooms = quote.data.numberOfBathrooms,
            extraBuildings = quote.data.extraBuildings?.map { extraBuilding ->
              ExtraBuilding(
                type = extraBuilding.type,
                area = extraBuilding.area,
                hasWaterConnected = extraBuilding.hasWaterConnected,
                displayName = extraBuilding.displayName
              )
            },
            ancillaryArea = quote.data.ancillaryArea,
            yearOfConstruction = quote.data.yearOfConstruction,
            isSubleted = quote.data.isSubleted,
            floor = quote.data.floor
          )
          else -> throw IllegalArgumentException("No such quote data type ${quote.data::class}")
        },
        state = QuoteState.valueOf(quote.state.toString()),
        productType = ProductType.valueOf(quote.productType.toString()),
        attributedTo = quote.attributedTo,
        initiatedFrom = quote.initiatedFrom.toString(),
        createdAt = quote.createdAt,
        validity = quote.validity,
        isComplete = quote.isComplete,
        breachedUnderwritingGuidelines = quote.breachedUnderwritingGuidelines,
        originatingProductId = quote.originatingProductId,
        signedProductId = quote.signedProductId
      )
  }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(value = QuoteData.ApartmentQuoteData::class, name = "apartment"),
  JsonSubTypes.Type(value = QuoteData.HouseQuoteData::class, name = "house")
)
@UnionType
sealed class QuoteData {
  @UnionType
  data class ApartmentQuoteData(
    val id: UUID,
    val ssn: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,

    val street: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val householdSize: Int? = null,
    val livingSpace: Int? = null,

    val subType: com.hedvig.backoffice.services.product_pricing.dto.ProductType? = null
  ) : QuoteData()

  @UnionType
  data class HouseQuoteData(
    val id: UUID,
    val ssn: String?,
    val firstName: String?,
    val lastName: String?,

    val street: String?,
    val city: String?,
    val zipCode: String?,
    val householdSize: Int?,
    val livingSpace: Int?,
    val ancillaryArea: Int? = null,
    val yearOfConstruction: Int? = null,
    val numberOfBathrooms: Int? = null,
    val extraBuildings: List<ExtraBuilding>? = null,
    val isSubleted: Boolean? = null,
    val floor: Int = 0
  ) : QuoteData()
}

