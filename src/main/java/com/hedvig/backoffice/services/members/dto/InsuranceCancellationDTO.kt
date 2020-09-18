package com.hedvig.backoffice.services.members.dto

import java.time.LocalDate
import java.util.*

data class InsuranceCancellationDTO (
  val memberId: Long? = null,
  val insuranceId: UUID? = null,
  val cancellationDate: LocalDate? = null
)
