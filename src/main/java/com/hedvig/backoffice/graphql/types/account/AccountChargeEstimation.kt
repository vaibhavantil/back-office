package com.hedvig.backoffice.graphql.types.account

import com.hedvig.backoffice.services.account.dto.AccountChargeEstimationDTO
import com.hedvig.graphql.commons.type.MonetaryAmountV2

data class AccountChargeEstimation(
  val subscription: MonetaryAmountV2,
  val discount: MonetaryAmountV2,
  val charge: MonetaryAmountV2,
  val discountCodes: List<String>
) {
  companion object {
    fun from(accountChargeEstimationDTO: AccountChargeEstimationDTO): AccountChargeEstimation {
      return AccountChargeEstimation(
        MonetaryAmountV2.of(accountChargeEstimationDTO.subscription),
        MonetaryAmountV2.of(accountChargeEstimationDTO.discount),
        MonetaryAmountV2.of(accountChargeEstimationDTO.charge),
        accountChargeEstimationDTO.discountCodes
      )
    }
  }
}
