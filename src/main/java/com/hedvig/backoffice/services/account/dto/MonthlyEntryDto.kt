package com.hedvig.backoffice.services.account.dto

import java.time.Instant
import java.util.*
import javax.money.MonetaryAmount

data class MonthlyEntryDto(
    val id: UUID,
    val externalId: String?,
    val amount: MonetaryAmount,
    val type: AccountEntryType,
    val source: String,
    val addedBy: String,
    val addedAt: Instant,
    val title: String,
    val comment: String
)
