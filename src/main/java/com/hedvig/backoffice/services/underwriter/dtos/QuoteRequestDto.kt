package com.hedvig.backoffice.services.underwriter.dtos

import com.hedvig.backoffice.services.product_pricing.dto.contract.ExtraBuilding
import java.time.LocalDate
import java.util.*

data class QuoteRequestDto(
  val firstName: String,
  val lastName: String,
  val currentInsurer: String?,
  val birthDate: LocalDate?,
  val ssn: String?,
  val quotingPartner: String?,
  val productType: ProductType?,
  val incompleteHouseQuoteData: IncompleteHouseQuoteDataDto?,
  val incompleteApartmentQuoteData: IncompleteApartmentQuoteDataDto?,
  val norwegianHomeContentsData: IncompleteNorwegianHomeContentsQuoteDataDto?,
  val norwegianTravelData: IncompleteNorwegianTravelQuoteDataDto?,
  val memberId: String? = null,
  val originatingProductId: UUID? = null,
  val underwritingGuidelinesBypassedBy: String?
)

data class IncompleteHouseQuoteDataDto(
  val street: String?,
  val zipCode: String?,
  val city: String?,
  val livingSpace: Int?,
  val personalNumber: String?,
  val householdSize: Int?,
  val ancillaryArea: Int?,
  val yearOfConstruction: Int?,
  val numberOfBathrooms: Int?,
  val extraBuildings: List<ExtraBuilding>?,
  val isSubleted: Boolean
) {
  companion object {
    fun from(dto: QuoteData.HouseData): IncompleteHouseQuoteDataDto =
      IncompleteHouseQuoteDataDto(
        street = dto.street,
        zipCode = dto.zipCode,
        city = dto.city,
        livingSpace = dto.livingSpace,
        householdSize = dto.householdSize,
        ancillaryArea = dto.ancillaryArea,
        yearOfConstruction = dto.yearOfConstruction,
        numberOfBathrooms = dto.numberOfBathrooms,
        extraBuildings = dto.extraBuildings,
        personalNumber = dto.ssn,
        isSubleted = dto.isSubleted ?: false
      )
  }
}

data class IncompleteApartmentQuoteDataDto(
  val street: String?,
  val zipCode: String?,
  val city: String?,
  val livingSpace: Int?,
  val householdSize: Int?,
  val subType: SwedishApartmentType?
) {
  companion object {
    fun from(dto: QuoteData.ApartmentData): IncompleteApartmentQuoteDataDto =
      IncompleteApartmentQuoteDataDto(
        street = dto.street,
        zipCode = dto.zipCode,
        city = dto.city,
        livingSpace = dto.livingSpace,
        householdSize = dto.householdSize,
        subType = dto.subType
      )
  }
}

data class IncompleteNorwegianHomeContentsQuoteDataDto(
  val street: String?,
  val zipCode: String?,
  val city: String?,
  val livingSpace: Int?,
  val coInsured: Int?,
  val type: NorwegianHomeContentType?,
  val isYouth: Boolean?
) {
  companion object {
    fun from(dto: QuoteData.NorwegianHomeContentData): IncompleteNorwegianHomeContentsQuoteDataDto =
      IncompleteNorwegianHomeContentsQuoteDataDto(
        street = dto.street,
        zipCode = dto.zipCode,
        city = dto.city,
        livingSpace = dto.livingSpace,
        coInsured = dto.coInsured,
        type = dto.type,
        isYouth = dto.isYouth
      )
  }
}

data class IncompleteNorwegianTravelQuoteDataDto(
  val coInsured: Int?,
  val isYouth: Boolean?
) {
  companion object {
    fun from(dto: QuoteData.NorwegianTravelData): IncompleteNorwegianTravelQuoteDataDto =
      IncompleteNorwegianTravelQuoteDataDto(
        coInsured = dto.coInsured,
        isYouth = dto.isYouth
      )
  }
}
