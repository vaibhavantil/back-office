package com.hedvig.backoffice.services.product_pricing.dto.contract

import java.time.LocalDate
import java.util.*

data class TerminateContractRequest(
  val contractId: UUID,
  val terminationDate: LocalDate,
  val terminationReason: TerminationReason,
  val comment: String?
)
