package com.hedvig.backoffice.services.underwriter.dtos

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.hedvig.backoffice.graphql.UnionType
import com.hedvig.backoffice.graphql.types.ApartmentQuoteDataInput
import com.hedvig.backoffice.graphql.types.HouseQuoteDataInput
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

enum class QuoteInitiatedFrom {
  RAPIO,
  WEBONBOARDING,
  APP,
  HOPE
}

data class ExtraBuilding(
  val type: String,
  val area: Int,
  val hasWaterConnected: Boolean,
  val displayName: String?
) {
  companion object {
    fun from(extraBuilding: com.hedvig.backoffice.graphql.types.ExtraBuilding): ExtraBuilding =
      ExtraBuilding(
        type = extraBuilding.type,
        area = extraBuilding.area,
        hasWaterConnected = extraBuilding.hasWaterConnected,
        displayName = extraBuilding.displayName
      )
  }
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
  val isComplete: Boolean,
  val signedProductId: UUID?,
  val originatingProductId: UUID?
)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(value = QuoteData.ApartmentQuoteData::class, name = "apartment"),
  JsonSubTypes.Type(value = QuoteData.HouseQuoteData::class, name = "house")
)
sealed class QuoteData {
  data class ApartmentQuoteData(
    val id: UUID?,
    val ssn: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,

    val street: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val householdSize: Int? = null,
    val livingSpace: Int? = null,

    val subType: com.hedvig.backoffice.services.product_pricing.dto.ProductType? = null
  ) : QuoteData() {
    companion object {
      fun from(apartmentQuoteDataInput: ApartmentQuoteDataInput): ApartmentQuoteData =
        ApartmentQuoteData(
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

  data class HouseQuoteData(
    val id: UUID?,
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
  ) : QuoteData() {
    companion object {
      fun from(houseQuoteDataInput: HouseQuoteDataInput): HouseQuoteData =
        HouseQuoteData(
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
          isSubleted = houseQuoteDataInput.isSubleted,
          floor = houseQuoteDataInput.floor
        )
    }
  }
}
