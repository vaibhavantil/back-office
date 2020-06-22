package com.hedvig.backoffice.graphql.types.account

import com.hedvig.backoffice.services.account.ChargeStatus
import java.time.Instant
import java.util.*
import javax.money.MonetaryAmount

data class SchedulerStatus(
  val id: UUID,
  val memberId: String,
  val status: ChargeStatus,
  val changedBy: String,
  val changedAt: Instant,
  val amount: MonetaryAmount?,
  val transactionId: UUID?
)
