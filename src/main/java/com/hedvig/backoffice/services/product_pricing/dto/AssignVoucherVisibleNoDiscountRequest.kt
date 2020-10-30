package com.hedvig.backoffice.services.product_pricing.dto

import com.hedvig.backoffice.graphql.types.AssignVoucherFreeMonths
import com.hedvig.backoffice.graphql.types.AssignVoucherVisibleNoDiscount
import java.time.Instant

data class AssignVoucherVisibleNoDiscountRequest(
  val partnerId: String,
  val code: String,
  val validFrom: Instant? = null,
  val validUntil: Instant? = null
) {
  companion object {
    fun from(voucherDiscount: AssignVoucherVisibleNoDiscount): AssignVoucherVisibleNoDiscountRequest {
      return AssignVoucherVisibleNoDiscountRequest(
        voucherDiscount.partnerId,
        voucherDiscount.code,
        voucherDiscount.validFrom,
        voucherDiscount.validUntil
      )
    }
  }
}
