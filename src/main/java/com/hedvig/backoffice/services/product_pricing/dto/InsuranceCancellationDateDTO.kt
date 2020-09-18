package com.hedvig.backoffice.services.product_pricing.dto

import java.time.Instant
import java.util.*

data class InsuranceCancellationDateDTO (
  val memberId: Long? = null,
  val insuranceId: UUID? = null,
  val cancellationDate: Instant? = null
)
