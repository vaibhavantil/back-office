package com.hedvig.backoffice.graphql.types.account

import com.hedvig.backoffice.services.account.dto.AccountEntryDTO
import com.hedvig.backoffice.services.account.dto.AccountEntryType
import com.hedvig.graphql.commons.type.MonetaryAmountV2
import java.time.Instant
import java.time.LocalDate
import java.util.*

data class AccountEntry(
  val id: UUID,
  val fromDate: LocalDate,
  val amount: MonetaryAmountV2,
  val type: AccountEntryType,
  val source: String,
  val reference: String,
  val title: String?,
  val comment: String?,
  val failedAt: Instant?,
  val chargedAt: Instant?
) {
  companion object {
    fun from(accountEntryDTO: AccountEntryDTO) = AccountEntry(
      id = accountEntryDTO.id,
      fromDate = accountEntryDTO.fromDate,
      amount = MonetaryAmountV2.of(accountEntryDTO.amount),
      type = accountEntryDTO.type,
      source = accountEntryDTO.source,
      reference = accountEntryDTO.reference,
      title = accountEntryDTO.title,
      comment = accountEntryDTO.comment,
      failedAt = accountEntryDTO.failedAt,
      chargedAt = accountEntryDTO.chargedAt
    )
  }
}
