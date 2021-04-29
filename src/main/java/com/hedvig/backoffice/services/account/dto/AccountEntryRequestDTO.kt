package com.hedvig.backoffice.services.account.dto

import com.hedvig.backoffice.graphql.types.account.AccountEntryInput
import java.time.LocalDate
import java.util.UUID
import javax.money.MonetaryAmount

data class AccountEntryRequestDTO(
    val fromDate: LocalDate,
    val amount: MonetaryAmount,
    val type: String,
    val source: String,
    val reference: String,
    val matchesEntryId: UUID?,
    val title: String?,
    val comment: String?,
    val addedBy: String
) {
    companion object {
        fun from(accountEntryInput: AccountEntryInput, addedBy: String): AccountEntryRequestDTO {
            return AccountEntryRequestDTO(
                fromDate = accountEntryInput.fromDate,
                amount = accountEntryInput.amount,
                type = accountEntryInput.type,
                source = accountEntryInput.source,
                reference = accountEntryInput.reference,
                matchesEntryId = null,
                title = accountEntryInput.title,
                comment = accountEntryInput.comment,
                addedBy = addedBy
            )
        }
    }
}
