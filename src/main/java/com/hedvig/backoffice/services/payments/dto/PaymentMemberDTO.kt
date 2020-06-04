package com.hedvig.backoffice.services.payments.dto

import java.util.*

data class PaymentMemberDTO(
  val id: String,
  var transactions: Map<UUID, TransactionDTO>,
  val directDebitMandateActive: Boolean,
  val trustlyAccountNumber: String?
)
