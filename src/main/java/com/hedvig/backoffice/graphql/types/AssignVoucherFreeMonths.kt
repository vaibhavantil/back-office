package com.hedvig.backoffice.graphql.types

import java.time.Instant

data class AssignVoucherFreeMonths(
  val partnerId: String,
  val numberOfFreeMonths: Int,
  val code: String,
  val validFrom: Instant? = null,
  val validUntil: Instant? = null
)
