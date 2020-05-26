package com.hedvig.backoffice.services.product_pricing.dto

import java.math.BigDecimal

data class Incentive(
val numberOfMonths: Int?,
val amount: BigDecimal?,
val percentageDiscount: BigDecimal?,
var currency: String?,
val type: IncentiveType?
)

enum class IncentiveType {
  COST_DEDUCTION,
  FREE_MONTHS,
  NO_DISCOUNT,
  MONTHLY_PERCENTAGE_DISCOUNT_FIXED_PERIOD,
  INDEFINITE_PERCENTAGE_DISCOUNT
}
