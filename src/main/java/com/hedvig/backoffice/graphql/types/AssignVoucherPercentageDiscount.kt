package com.hedvig.backoffice.graphql.types

import java.time.Instant

data class AssignVoucherPercentageDiscount(
  val partnerId: String,
  val numberOfMonths: Int,
  val percentageDiscount: Float,
  val code: String,
  val validFrom: Instant? = null,
  val validUntil: Instant? = null
)
