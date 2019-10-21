package com.hedvig.backoffice.services.account.dto

import java.time.Instant

data class NumberFailedChargesDto(
  val memberId: String,
  val numberFailedCharges: Int,
  val lastFailedChargeAt: Instant?
)
