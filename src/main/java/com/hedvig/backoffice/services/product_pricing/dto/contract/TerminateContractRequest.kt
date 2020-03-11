package com.hedvig.backoffice.services.product_pricing.dto.contract

import java.time.LocalDate

data class TerminateContractRequest(
  val terminationDate: LocalDate,
  val terminationReason: TerminationReason,
  val comment: String?
)
