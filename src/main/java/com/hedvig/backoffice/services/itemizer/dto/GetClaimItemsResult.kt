package com.hedvig.backoffice.services.itemizer.dto

import java.math.BigDecimal

data class GetClaimItemsResult(
    val claimItems: List<ClaimItem>,
    val totalValuation: BigDecimal?
)
