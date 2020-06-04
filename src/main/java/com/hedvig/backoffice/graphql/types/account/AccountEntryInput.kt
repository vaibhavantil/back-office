package com.hedvig.backoffice.graphql.types.account

import com.hedvig.backoffice.services.account.dto.AccountEntryType
import java.time.LocalDate
import javax.money.MonetaryAmount

data class AccountEntryInput(
  val type: AccountEntryType,
  val amount: MonetaryAmount,
  val fromDate: LocalDate,
  val reference: String,
  val source: String,
  val title: String?,
  val comment: String?
)
