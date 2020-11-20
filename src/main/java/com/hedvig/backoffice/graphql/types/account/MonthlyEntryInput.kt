package com.hedvig.backoffice.graphql.types.account

import com.hedvig.backoffice.services.account.dto.AccountEntryType
import javax.money.MonetaryAmount

data class MonthlyEntryInput(
    val externalId: String?,
    val amount: MonetaryAmount,
    val type: AccountEntryType,
    val source: String,
    val title: String,
    val comment: String
)
