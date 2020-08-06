package com.hedvig.backoffice.graphql.types

import java.time.Instant

data class AssignVoucherVisibleNoDiscount(
  val partnerId: String,
  val code: String,
  val validFrom: Instant? = null,
  val validUntil: Instant? = null
)
