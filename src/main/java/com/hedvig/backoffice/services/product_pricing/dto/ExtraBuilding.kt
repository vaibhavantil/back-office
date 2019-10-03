package com.hedvig.backoffice.services.product_pricing.dto

data class ExtraBuilding(
  val type: ExtraBuildingType,
  val area: Int,
  val hasWaterConnected: Boolean
)
