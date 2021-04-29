package com.hedvig.backoffice.graphql.types.account

import java.time.LocalDate
import javax.money.MonetaryAmount

data class AccountEntryInput(
  val type: String,
  val amount: MonetaryAmount,
  val fromDate: LocalDate,
  val reference: String,
  val source: String,
  val title: String?,
  val comment: String?
)
