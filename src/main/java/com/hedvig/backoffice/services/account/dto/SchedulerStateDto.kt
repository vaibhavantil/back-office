package com.hedvig.backoffice.services.account.dto

import com.hedvig.backoffice.services.account.ChargeStatus
import java.time.Instant
import java.util.UUID
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
