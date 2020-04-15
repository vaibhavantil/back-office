package com.hedvig.backoffice.graphql.types.account

import com.hedvig.backoffice.services.account.dto.AccountDTO
import com.hedvig.graphql.commons.type.MonetaryAmountV2

data class Account(
  var id: String,
  var currentBalance: MonetaryAmountV2,
  var totalBalance: MonetaryAmountV2,
  var chargeEstimation: AccountChargeEstimation,
  var entries: List<AccountEntry>
) {
  companion object {
    fun from(account: AccountDTO) = Account(
      id = account.memberId,
      currentBalance = MonetaryAmountV2.of(account.currentBalance),
      totalBalance = MonetaryAmountV2.of(account.totalBalance),
      chargeEstimation = AccountChargeEstimation.from(account.chargeEstimation),
      entries = account.entries.map { accountEntryDTO -> AccountEntry.from(accountEntryDTO) }
    )
  }
}
