package com.hedvig.backoffice.services.payments.dto

import java.time.Instant
import java.util.*
import javax.money.MonetaryAmount

data class TransactionDTO(
  var id: UUID,
  var amount: MonetaryAmount,
  var timestamp: Instant,
  var transactionType: String,
  var transactionStatus: String
)
