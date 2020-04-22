package com.hedvig.backoffice.services.product_pricing.dto

import javax.money.MonetaryAmount

data class MonthlySubscriptionDTO(
  var memberId: String,
  var subscription: MonetaryAmount
)
