package com.hedvig.backoffice.services.product_pricing.dto

import java.util.*

data class ExtraBuildingDTO(
  val id: UUID?,
  val type: ExtraBuildingType,
  val area: Int,
  val hasWaterConnected: Boolean,
  val displayName: String?
)
