package com.hedvig.backoffice.services.itemizer.dto

import com.hedvig.graphql.commons.type.MonetaryAmountV2

data class ClaimItemValuation(
  val depreciatedValue: MonetaryAmountV2,
  val valuationRule: ValuationRule?,
  val explanation: String?
)
