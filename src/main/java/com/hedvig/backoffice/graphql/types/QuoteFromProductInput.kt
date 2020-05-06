package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.underwriter.dtos.NorwegianHomeContentType
import com.hedvig.backoffice.services.underwriter.dtos.SwedishApartmentType
import com.hedvig.backoffice.services.product_pricing.dto.contract.ExtraBuilding
import java.util.UUID

data class QuoteFromProductInput(
  val incompleteHouseQuoteData: HouseQuoteDataInput?,
  val incompleteApartmentQuoteData: ApartmentQuoteDataInput?,
  val norwegianHomeContentQuoteData: NorwegianHomeContentQuoteDataInput?,
  val norwegianTravelQuoteData: NorwegianTravelQuoteDataInput?,
  val originatingProductId: UUID,
  val currentInsurer: String?
)

data class ApartmentQuoteDataInput(
  val ssn: String? = null,
  val firstName: String? = null,
  val lastName: String? = null,

  val street: String? = null,
  val city: String? = null,
  val zipCode: String? = null,
  val householdSize: Int? = null,
  val livingSpace: Int? = null,
  val subType: SwedishApartmentType? = null
)

data class HouseQuoteDataInput(
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
)

data class NorwegianHomeContentQuoteDataInput(
  val ssn: String? = null,
  val firstName: String? = null,
  val lastName: String? = null,

  val street: String? = null,
  val city: String? = null,
  val zipCode: String? = null,
  val householdSize: Int? = null,
  val livingSpace: Int? = null,
  val type: NorwegianHomeContentType? = null
)

data class NorwegianTravelQuoteDataInput(
  val ssn: String? = null,
  val firstName: String? = null,
  val lastName: String? = null,

  val householdSize: Int? = null,
  val isYouth: Boolean? = null
)
