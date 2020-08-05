package com.hedvig.backoffice.services.product_pricing.dto

import com.hedvig.backoffice.graphql.types.AssignVoucherFreeMonths
import com.hedvig.backoffice.graphql.types.AssignVoucherPercentageDiscount
import java.math.BigDecimal
import java.time.Instant

data class AssignVoucherFreeMonthsRequest(
  val partnerId: String,
  val numberOfFreeMonths: Int,
  val code: String,
  val validFrom: Instant? = null,
  val validUntil: Instant? = null
) {
  companion object {
    fun from(voucherDiscount: AssignVoucherFreeMonths): AssignVoucherFreeMonthsRequest {
      return AssignVoucherFreeMonthsRequest(
        voucherDiscount.partnerId,
        voucherDiscount.numberOfFreeMonths,
        voucherDiscount.code,
        voucherDiscount.validFrom,
        voucherDiscount.validUntil
      )
    }
  }
}
