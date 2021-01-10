package com.hedvig.backoffice.services.itemizer.dto

import com.hedvig.graphql.commons.type.MonetaryAmountV2

data class TotalClaimItemValuation(
    val totalValuation: MonetaryAmountV2?,
    val deductible: MonetaryAmountV2?
)
