package com.hedvig.backoffice.services.underwriter.dtos

import com.hedvig.backoffice.graphql.types.QuoteInput
import com.hedvig.backoffice.graphql.types.QuoteInputData
import com.hedvig.backoffice.services.product_pricing.dto.contract.ExtraBuilding
import com.hedvig.backoffice.services.product_pricing.dto.contract.NorwegianHomeContentLineOfBusiness
import com.hedvig.backoffice.services.product_pricing.dto.contract.NorwegianTravelLineOfBusiness
import java.time.LocalDate
import java.util.UUID

data class QuoteRequestDto(
  val firstName: String?,
  val lastName: String?,
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
) {
  companion object {
    fun from(input: QuoteInput, memberId: String, underwritingGuidelinesBypassedBy: String?): QuoteRequestDto {
      return QuoteRequestDto(
        firstName = null,
        lastName = null,
        currentInsurer = input.currentInsurer,
        birthDate = null,
        ssn = null,
        quotingPartner = null,
        productType = input.productType?.toString()?.let(ProductType::valueOf),
        incompleteHouseQuoteData = input.houseData?.let((IncompleteHouseQuoteDataDto)::from),
        incompleteApartmentQuoteData = input.apartmentData?.let((IncompleteApartmentQuoteDataDto)::from),
        norwegianHomeContentsData = input.norwegianHomeContentData?.let((IncompleteNorwegianHomeContentsQuoteDataDto)::from),
        norwegianTravelData = input.norwegianTravelData?.let((IncompleteNorwegianTravelQuoteDataDto)::from),
        memberId = memberId,
        originatingProductId = null,
        underwritingGuidelinesBypassedBy = underwritingGuidelinesBypassedBy
      )
    }
  }
}

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

    fun from(input: QuoteInputData.HouseQuoteInput): IncompleteHouseQuoteDataDto =
      IncompleteHouseQuoteDataDto(
        street = input.street,
        zipCode = input.zipCode,
        city = input.city,
        livingSpace = input.livingSpace,
        householdSize = input.householdSize,
        ancillaryArea = input.ancillaryArea,
        yearOfConstruction = input.yearOfConstruction,
        numberOfBathrooms = input.numberOfBathrooms,
        extraBuildings = input.extraBuildings,
        personalNumber = null,
        isSubleted = input.isSubleted ?: false
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

    fun from(input: QuoteInputData.ApartmentQuoteInput): IncompleteApartmentQuoteDataDto =
      IncompleteApartmentQuoteDataDto(
        street = input.street,
        zipCode = input.zipCode,
        city = input.city,
        livingSpace = input.livingSpace,
        householdSize = input.householdSize,
        subType = input.subType
      )
  }
}

data class IncompleteNorwegianHomeContentsQuoteDataDto(
  val street: String?,
  val zipCode: String?,
  val city: String?,
  val livingSpace: Int?,
  val coInsured: Int?,
  val subType: NorwegianHomeContentLineOfBusiness?,
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
        subType = dto.type,
        isYouth = dto.isYouth
      )

    fun from(input: QuoteInputData.NorwegianHomeContentQuoteInput): IncompleteNorwegianHomeContentsQuoteDataDto =
      IncompleteNorwegianHomeContentsQuoteDataDto(
        street = input.street,
        zipCode = input.zipCode,
        city = input.city,
        livingSpace = input.livingSpace,
        coInsured = input.householdSize?.minus(1),
        subType = input.subType,
        isYouth = when(input.subType) {
          NorwegianHomeContentLineOfBusiness.OWN, NorwegianHomeContentLineOfBusiness.RENT -> false
          NorwegianHomeContentLineOfBusiness.YOUTH_OWN, NorwegianHomeContentLineOfBusiness.YOUTH_RENT -> true
          else -> null
        }
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

    fun from(input: QuoteInputData.NorwegianTravelQuoteInput): IncompleteNorwegianTravelQuoteDataDto =
      IncompleteNorwegianTravelQuoteDataDto(
        coInsured = input.householdSize?.minus(1),
        isYouth = when(input.subType) {
          NorwegianTravelLineOfBusiness.REGULAR-> false
          NorwegianTravelLineOfBusiness.YOUTH -> true
          else -> null
        }
      )
  }
}
