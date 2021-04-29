package com.hedvig.backoffice.services.account.dto

import com.hedvig.backoffice.graphql.types.account.MonthlyEntryInput
import javax.money.MonetaryAmount

data class AddMonthlyEntryRequest(
    val externalId: String?,
    val amount: MonetaryAmount,
    val type: String,
    val source: String,
    val title: String,
    val comment: String
) {
    companion object {
        fun from(addMonthlyEntryInput: MonthlyEntryInput) = AddMonthlyEntryRequest(
            externalId = addMonthlyEntryInput.externalId,
            amount = addMonthlyEntryInput.amount,
            type = addMonthlyEntryInput.type,
            source = addMonthlyEntryInput.source,
            title = addMonthlyEntryInput.title,
            comment = addMonthlyEntryInput.comment
        )
    }
}
