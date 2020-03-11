package com.hedvig.backoffice.services.product_pricing.dto.contract

import java.time.LocalDate
import java.util.UUID

class ActivatePendingAgreementRequest(
  val pendingAgreementId: UUID,
  val fromDate: LocalDate
)
