package com.hedvig.backoffice.services.product_pricing.dto

import com.hedvig.backoffice.graphql.types.AssignVoucherPercentageDiscount
import java.math.BigDecimal
import java.time.Instant

data class AssignVoucherPercentageDiscountRequest(
  val partnerId: String,
  val numberOfMonths: Int,
  val percentageDiscount: BigDecimal,
  val code: String,
  val validFrom: Instant? = null,
  val validUntil: Instant? = null
) {
  companion object {
    fun from(voucherDiscount: AssignVoucherPercentageDiscount): AssignVoucherPercentageDiscountRequest {
      return AssignVoucherPercentageDiscountRequest(
        voucherDiscount.partnerId,
        voucherDiscount.numberOfMonths,
        voucherDiscount.percentageDiscount.toBigDecimal(),
        voucherDiscount.code,
        voucherDiscount.validFrom,
        voucherDiscount.validUntil
      )
    }
  }
}
