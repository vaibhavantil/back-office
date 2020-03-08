package com.hedvig.backoffice.services.product_pricing.dto.contract

import java.time.LocalDate
import java.util.UUID

class ChangeTerminationDateRequest(
  val contractId: UUID,
  val newTerminationDate: LocalDate,
  val dangerouslyAllowHistoryChange: Boolean = false
)
