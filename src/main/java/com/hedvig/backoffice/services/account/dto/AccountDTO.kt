package com.hedvig.backoffice.services.account.dto

import javax.money.MonetaryAmount

data class AccountDTO(
    val memberId: String,
    val currentBalance: MonetaryAmount,
    val totalBalance: MonetaryAmount,
    val entries: List<AccountEntryDTO>,
    val chargeEstimation: AccountChargeEstimationDTO
)
