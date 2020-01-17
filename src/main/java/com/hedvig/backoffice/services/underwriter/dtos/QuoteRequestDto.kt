package com.hedvig.backoffice.services.underwriter.dtos

import com.hedvig.backoffice.services.product_pricing.dto.ProductType
import java.time.LocalDate
import java.util.UUID

data class QuoteRequestDto(
  val firstName: String,
  val lastName: String,
  val currentInsurer: String?,
  val birthDate: LocalDate?,
  val ssn: String?,
  val quotingPartner: String?,
  val productType: com.hedvig.backoffice.services.underwriter.dtos.ProductType?,
  val incompleteHouseQuoteData: IncompleteHouseQuoteDataDto?,
  val incompleteApartmentQuoteData: IncompleteApartmentQuoteDataDto?,
  val memberId: String? = null,
  val originatingProductId: UUID? = null,
  val shouldComplete: Boolean = true,
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
  val isSubleted: Boolean,
  val floor: Int = 0
) {
  companion object {
    fun from(dto: QuoteData.HouseQuoteData): IncompleteHouseQuoteDataDto =
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
        floor = dto.floor,
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
  val floor: Int?,
  val subType: ProductType?
) {
  companion object {
    fun from(dto: QuoteData.ApartmentQuoteData): IncompleteApartmentQuoteDataDto =
      IncompleteApartmentQuoteDataDto(
        street = dto.street,
        zipCode = dto.zipCode,
        city = dto.city,
        livingSpace = dto.livingSpace,
        householdSize = dto.householdSize,
        floor = null,
        subType = dto.subType
      )
  }
}
