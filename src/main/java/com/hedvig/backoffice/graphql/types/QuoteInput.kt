package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.graphql.UnionType
import java.math.BigDecimal
import java.util.UUID

data class QuoteInput(
  val price: BigDecimal? = null,
  val productType: ProductType?,
  val apartmentData: QuoteInputData.ApartmentQuoteInput?,
  val houseData: QuoteInputData.HouseQuoteInput?,
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

    val subType: com.hedvig.backoffice.services.product_pricing.dto.ProductType? = null
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
}

