package com.hedvig.backoffice.graphql.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.hedvig.backoffice.graphql.UnionType
import com.hedvig.backoffice.services.underwriter.dtos.NorwegianHomeContentType
import com.hedvig.backoffice.services.product_pricing.dto.contract.ExtraBuilding
import com.hedvig.backoffice.services.underwriter.dtos.QuoteDto
import com.hedvig.backoffice.services.underwriter.dtos.SwedishApartmentType
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.UUID


enum class ProductType {
  APARTMENT,
  HOUSE,
  OBJECT,
  HOME_CONTENT,
  TRAVEL,
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
        data = when (quote.data) {
            is com.hedvig.backoffice.services.underwriter.dtos.QuoteData.ApartmentData -> QuoteData.ApartmentQuoteData(
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
          is com.hedvig.backoffice.services.underwriter.dtos.QuoteData.HouseData -> QuoteData.HouseQuoteData(
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
                isSubleted = quote.data.isSubleted
          )
          is com.hedvig.backoffice.services.underwriter.dtos.QuoteData.NorwegianHomeContentData -> QuoteData.NorwegianHomeContentQuoteData(
            id = quote.data.id!!,
            ssn = quote.data.ssn,
            firstName = quote.data.firstName,
            lastName = quote.data.lastName,
            street = quote.data.street,
            zipCode = quote.data.zipCode,
            city = quote.data.city,
            livingSpace = quote.data.livingSpace,
            householdSize = quote.data.coInsured?.plus(1),
            type = when (quote.data.type) {
              NorwegianHomeContentType.RENT -> if (quote.data.isStudent != null && quote.data.isStudent) NorwegianHomeContentType.STUDENT_RENT else NorwegianHomeContentType.RENT
              NorwegianHomeContentType.OWN -> if (quote.data.isStudent != null && quote.data.isStudent) NorwegianHomeContentType.STUDENT_OWN else NorwegianHomeContentType.OWN
              NorwegianHomeContentType.STUDENT_RENT -> NorwegianHomeContentType.STUDENT_RENT
              NorwegianHomeContentType.STUDENT_OWN -> NorwegianHomeContentType.STUDENT_OWN
              else -> null
            }
          )
          is com.hedvig.backoffice.services.underwriter.dtos.QuoteData.NorwegianTravelData -> QuoteData.NorwegianTravelQuoteData(
            id = quote.data.id!!,
            ssn = quote.data.ssn,
            firstName = quote.data.firstName,
            lastName = quote.data.lastName,
            householdSize = quote.data.coInsured?.plus(1)
          )
          else -> throw RuntimeException("Expecting a valid quote data type but got ${quote.javaClass}")
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
  JsonSubTypes.Type(value = QuoteData.HouseQuoteData::class, name = "house"),
  JsonSubTypes.Type(value = QuoteData.NorwegianHomeContentQuoteData::class, name = "norwegianHomeContent"),
  JsonSubTypes.Type(value = QuoteData.NorwegianTravelQuoteData::class, name = "norwegianTravel")
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

    val subType: SwedishApartmentType? = null
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
    val isSubleted: Boolean? = null
  ) : QuoteData()

  @UnionType
  data class NorwegianHomeContentQuoteData(
    val id: UUID,
    val ssn: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,

    val street: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val householdSize: Int? = null,
    val livingSpace: Int? = null,

    val type: NorwegianHomeContentType? = null
  ) : QuoteData()


  @UnionType
  data class NorwegianTravelQuoteData(
    val id: UUID,
    val ssn: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,

    val householdSize: Int? = null
  ) : QuoteData()
}

