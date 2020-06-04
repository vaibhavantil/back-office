package com.hedvig.backoffice.services.payments.dto

import java.time.Instant
import java.util.*
import javax.money.MonetaryAmount

data class Transaction(
  var id: UUID,
  var amount: MonetaryAmount,
  var timestamp: Instant,
  var type: String,
  var status: String
) {
  companion object {
    fun fromTransactionDTO(id: UUID, dto: TransactionDTO) = Transaction(
      id = id,
      amount = dto.amount,
      timestamp = dto.timestamp,
      type = dto.transactionType,
      status = dto.transactionStatus)
  }
}
