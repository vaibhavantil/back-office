package com.hedvig.backoffice.services.account.dto

import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import javax.money.MonetaryAmount

data class AccountEntryDTO(
    val id: UUID,
    val fromDate: LocalDate,
    val amount: MonetaryAmount,
    val type: String,
    val source: String,
    val reference: String,
    val title: String?,
    val comment: String?,
    val addedBy: String,
    val failedAt: Instant?,
    val chargedAt: Instant?
)
