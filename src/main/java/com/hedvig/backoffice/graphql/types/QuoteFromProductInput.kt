package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.product_pricing.dto.contract.ExtraBuilding
import java.util.UUID

data class QuoteFromProductInput(
  val incompleteHouseQuoteData: HouseQuoteDataInput?,
  val incompleteApartmentQuoteData: ApartmentQuoteDataInput?,
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

  val subType: com.hedvig.backoffice.services.product_pricing.dto.ProductType? = null
)

data class HouseQuoteDataInput(
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
)
