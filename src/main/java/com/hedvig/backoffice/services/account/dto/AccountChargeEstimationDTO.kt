package com.hedvig.backoffice.services.account.dto

import javax.money.MonetaryAmount

data class AccountChargeEstimationDTO(
  val subscription: MonetaryAmount,
  val discount: MonetaryAmount,
  val charge: MonetaryAmount,
  val discountCodes: List<String>
)
