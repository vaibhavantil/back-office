package com.hedvig.backoffice.services.underwriter.dtos

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.hedvig.backoffice.graphql.types.NorwegianHomeContentQuoteDataInput
import com.hedvig.backoffice.graphql.types.NorwegianTravelQuoteDataInput
import com.hedvig.backoffice.graphql.types.ApartmentQuoteDataInput
import com.hedvig.backoffice.graphql.types.HouseQuoteDataInput
import com.hedvig.backoffice.services.product_pricing.dto.contract.ExtraBuilding
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.*

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

enum class QuoteInitiatedFrom {
  RAPIO,
  WEBONBOARDING,
  WEB,
  APP,
  IOS,
  ANDROID,
  HOPE
}

enum class ExtraBuildingType {
  GARAGE,
  CARPORT,
  SHED,
  STOREHOUSE,
  FRIGGEBOD,
  ATTEFALL,
  OUTHOUSE,
  GUESTHOUSE,
  GAZEBO,
  GREENHOUSE,
  SAUNA,
  BARN,
  BOATHOUSE,
  OTHER
}

enum class SwedishApartmentType {
  BRF,
  RENT,
  STUDENT_BRF,
  STUDENT_RENT
}

enum class NorwegianHomeContentType {
  OWN,
  RENT,
  YOUTH_OWN,
  YOUTH_RENT
}

data class QuoteDto(
  val id: UUID,
  val createdAt: Instant,
  val price: BigDecimal? = null,
  val productType: ProductType,
  val state: QuoteState,
  val initiatedFrom: QuoteInitiatedFrom,
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
)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(value = QuoteData.ApartmentData::class, name = "apartment"),
  JsonSubTypes.Type(value = QuoteData.HouseData::class, name = "house"),
  JsonSubTypes.Type(value = QuoteData.NorwegianHomeContentData::class, name = "norwegianHomeContentsData"),
  JsonSubTypes.Type(value = QuoteData.NorwegianTravelData::class, name = "norwegianTravelData")
)
sealed class QuoteData {
  data class ApartmentData(
    val id: UUID? = null,
    val ssn: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,

    val street: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val householdSize: Int? = null,
    val livingSpace: Int? = null,

    val subType: SwedishApartmentType? = null
  ) : QuoteData() {
    companion object {
      fun from(apartmentQuoteDataInput: ApartmentQuoteDataInput): ApartmentData =
        ApartmentData(
          id = null,
          ssn = apartmentQuoteDataInput.ssn,
          firstName = apartmentQuoteDataInput.firstName,
          lastName = apartmentQuoteDataInput.lastName,
          street = apartmentQuoteDataInput.street,
          zipCode = apartmentQuoteDataInput.zipCode,
          city = apartmentQuoteDataInput.city,
          livingSpace = apartmentQuoteDataInput.livingSpace,
          householdSize = apartmentQuoteDataInput.householdSize,
          subType = apartmentQuoteDataInput.subType
        )
    }
  }

  data class HouseData(
    val id: UUID? = null,
    val ssn: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,

    val street: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val householdSize: Int? = null,
    val livingSpace: Int? = null,
    val ancillaryArea: Int? = null,
    val yearOfConstruction: Int? = null,
    val numberOfBathrooms: Int? = null,
    val extraBuildings: List<ExtraBuilding>? = null,
    val isSubleted: Boolean? = null
  ) : QuoteData() {
    companion object {
      fun from(houseQuoteDataInput: HouseQuoteDataInput): HouseData =
        HouseData(
          id = null,
          ssn = houseQuoteDataInput.ssn,
          firstName = houseQuoteDataInput.firstName,
          lastName = houseQuoteDataInput.lastName,

          street = houseQuoteDataInput.street,
          zipCode = houseQuoteDataInput.zipCode,
          city = houseQuoteDataInput.city,
          livingSpace = houseQuoteDataInput.livingSpace,
          householdSize = houseQuoteDataInput.householdSize,
          numberOfBathrooms = houseQuoteDataInput.numberOfBathrooms,
          extraBuildings = houseQuoteDataInput.extraBuildings?.map { extraBuilding ->
            ExtraBuilding(
              type = extraBuilding.type,
              area = extraBuilding.area,
              hasWaterConnected = extraBuilding.hasWaterConnected,
              displayName = extraBuilding.displayName
            )
          },
          ancillaryArea = houseQuoteDataInput.ancillaryArea,
          yearOfConstruction = houseQuoteDataInput.yearOfConstruction,
          isSubleted = houseQuoteDataInput.isSubleted
        )
    }
  }

  data class NorwegianHomeContentData(
    val id: UUID? = null,
    val ssn: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,

    val street: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val livingSpace: Int? = null,
    val coInsured: Int? = null,
    val isYouth: Boolean? = null,
    val type: NorwegianHomeContentType? = null
  ) : QuoteData() {
    companion object {
      fun from(norwegianHomeContentQuoteDataInput: NorwegianHomeContentQuoteDataInput): NorwegianHomeContentData =
        NorwegianHomeContentData(
          id = null,
          ssn = norwegianHomeContentQuoteDataInput.ssn,
          firstName = norwegianHomeContentQuoteDataInput.firstName,
          lastName = norwegianHomeContentQuoteDataInput.lastName,

          street = norwegianHomeContentQuoteDataInput.street,
          zipCode = norwegianHomeContentQuoteDataInput.zipCode,
          city = norwegianHomeContentQuoteDataInput.city,
          livingSpace = norwegianHomeContentQuoteDataInput.livingSpace,
          coInsured = norwegianHomeContentQuoteDataInput.householdSize?.minus(1),
          type = when (norwegianHomeContentQuoteDataInput.subType) {
            NorwegianHomeContentType.RENT, NorwegianHomeContentType.YOUTH_RENT -> NorwegianHomeContentType.RENT
            NorwegianHomeContentType.OWN, NorwegianHomeContentType.YOUTH_OWN -> NorwegianHomeContentType.OWN
            null -> null
          },
          isYouth = when (norwegianHomeContentQuoteDataInput.subType) {
            NorwegianHomeContentType.YOUTH_RENT, NorwegianHomeContentType.YOUTH_OWN -> true
            NorwegianHomeContentType.RENT, NorwegianHomeContentType.OWN-> false
            null -> null
          }
        )
    }
  }

  data class NorwegianTravelData(
    val id: UUID? = null,
    val ssn: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,

    val coInsured: Int? = null,
    val isYouth: Boolean? = null
  ) : QuoteData() {
    companion object {
      fun from(norwegianTravelQuoteDataInput: NorwegianTravelQuoteDataInput): NorwegianTravelData =
        NorwegianTravelData(
          id = null,
          ssn = norwegianTravelQuoteDataInput.ssn,
          firstName = norwegianTravelQuoteDataInput.firstName,
          lastName = norwegianTravelQuoteDataInput.lastName,
          coInsured = norwegianTravelQuoteDataInput.householdSize?.minus(1),
          isYouth = norwegianTravelQuoteDataInput.isYouth
        )
    }
  }
}
