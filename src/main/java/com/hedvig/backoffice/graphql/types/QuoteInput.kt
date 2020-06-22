package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.underwriter.dtos.SwedishApartmentType
import com.hedvig.backoffice.services.product_pricing.dto.contract.ExtraBuilding
import com.hedvig.backoffice.services.product_pricing.dto.contract.NorwegianHomeContentLineOfBusiness
import com.hedvig.backoffice.services.product_pricing.dto.contract.NorwegianTravelLineOfBusiness
import java.math.BigDecimal
import java.util.*

data class QuoteInput(
  val price: BigDecimal? = null,
  val productType: ProductType?,
  val apartmentData: QuoteInputData.ApartmentQuoteInput?,
  val houseData: QuoteInputData.HouseQuoteInput?,
  val norwegianHomeContentData: QuoteInputData.NorwegianHomeContentQuoteInput?,
  val norwegianTravelData: QuoteInputData.NorwegianTravelQuoteInput?,
  val currentInsurer: String? = null,
  val originatingProductId: UUID?
)

sealed class QuoteInputData {
  data class ApartmentQuoteInput(
    val street: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val householdSize: Int? = null,
    val livingSpace: Int? = null,

    val subType: SwedishApartmentType? = null
  ) : QuoteInputData()

  data class HouseQuoteInput(
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
  ) : QuoteInputData()

  data class NorwegianHomeContentQuoteInput(
    val street: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val householdSize: Int? = null,
    val livingSpace: Int? = null,
    val subType: NorwegianHomeContentLineOfBusiness? = null
  ) : QuoteInputData()

  data class NorwegianTravelQuoteInput(
    val householdSize: Int? = null,
    val subType: NorwegianTravelLineOfBusiness? = null
  ) : QuoteInputData()
}

