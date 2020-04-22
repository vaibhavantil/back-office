package com.hedvig.backoffice.services.account.dto

import com.hedvig.backoffice.services.account.ChargeStatus
import lombok.Value
import java.time.Instant
import java.util.*
import javax.money.MonetaryAmount

data class SchedulerStateDto(
  var stateId: UUID,
  var memberId: String,
  var status: ChargeStatus,
  var changedBy: String,
  var changedAt: Instant,
  var amount: MonetaryAmount?,
  var transactionId: UUID?
)
