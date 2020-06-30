package com.hedvig.backoffice.services.itemizer.dto

import java.math.BigDecimal

data class ClaimItemValuation (
  val depreciatedValue: BigDecimal?,
  val valuationRule: ValuationRule?
)
