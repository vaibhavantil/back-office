package com.hedvig.backoffice.services.itemizer.dto

import com.hedvig.graphql.commons.type.MonetaryAmountV2

data class ClaimValuation(
  val totalValuation: MonetaryAmountV2?,
  val deductible: MonetaryAmountV2?
)
