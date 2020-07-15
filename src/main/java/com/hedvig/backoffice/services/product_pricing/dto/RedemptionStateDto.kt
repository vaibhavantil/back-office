package com.hedvig.backoffice.services.product_pricing.dto

import java.time.Instant

data class RedemptionStateDto (
  val redeemedAt: Instant,
  val activatedAt: Instant?,
  val activeTo: Instant?,
  val terminatedAt: Instant?,
  val unRedeemedAt: Instant?
)
