package com.hedvig.backoffice.graphql.types.account

import javax.money.MonetaryAmount

data class MonthlyEntryInput(
    val externalId: String?,
    val amount: MonetaryAmount,
    val type: String,
    val source: String,
    val title: String,
    val comment: String
)
