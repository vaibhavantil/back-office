package com.hedvig.backoffice.graphql.types

import com.hedvig.graphql.commons.type.MonetaryAmountV2
import java.time.Instant
import java.util.*
import com.hedvig.backoffice.services.payments.dto.Transaction as TransactionDto

data class Transaction(
  var id: UUID,
  var amount: MonetaryAmountV2,
  var timestamp: Instant,
  var type: String,
  var status: String
) {
  companion object {
    fun fromDTO(dto: TransactionDto) = Transaction(
      id = dto.id,
      amount = MonetaryAmountV2.of(dto.amount),
      timestamp = dto.timestamp,
      type = dto.type,
      status = dto.status
    )
  }
}
