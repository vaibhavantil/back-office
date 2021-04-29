package com.hedvig.backoffice.graphql.types.account

import com.hedvig.backoffice.services.account.dto.MonthlyEntryDto
import com.hedvig.graphql.commons.type.MonetaryAmountV2
import java.time.Instant
import java.util.*

data class MonthlyEntry(
    val id: UUID,
    val externalId: String?,
    val amount: MonetaryAmountV2,
    val type: String,
    val source: String,
    val addedBy: String,
    val addedAt: Instant,
    val title: String,
    val comment: String
) {
    companion object {
        fun from(monthlyEntry: MonthlyEntryDto) = MonthlyEntry(
            id = monthlyEntry.id,
            externalId = monthlyEntry.externalId,
            amount = MonetaryAmountV2.of(monthlyEntry.amount),
            type = monthlyEntry.type,
            source = monthlyEntry.source,
            addedBy = monthlyEntry.addedBy,
            addedAt = monthlyEntry.addedAt,
            title = monthlyEntry.title,
            comment = monthlyEntry.comment
        )
    }
}
