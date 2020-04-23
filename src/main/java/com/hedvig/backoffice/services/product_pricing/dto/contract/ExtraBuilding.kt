package com.hedvig.backoffice.services.product_pricing.dto.contract

import java.util.*

data class ExtraBuilding(
  val id: UUID? = null,
  val type: ExtraBuildingType,
  val area: Int,
  val hasWaterConnected: Boolean,
  val displayName: String? = null
)
