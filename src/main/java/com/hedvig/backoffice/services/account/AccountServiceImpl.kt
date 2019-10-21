package com.hedvig.backoffice.services.account

import com.hedvig.backoffice.graphql.types.account.AccountEntryInput
import com.hedvig.backoffice.services.account.dto.*

class AccountServiceImpl(private val accountServiceClient: AccountServiceClient) : AccountService {

  override fun getAccount(memberId: String): AccountDTO =
    accountServiceClient.getAccount(memberId)

  override fun batchFindCurrentBalances(memberIds: List<String>): List<AccountDTO> =
    accountServiceClient.batchFindCurrentBalances(memberIds)

  override fun addAccountEntry(memberId: String, accountEntryInput: AccountEntryInput, addedBy: String) =
    accountServiceClient.addAccountEntry(memberId, AccountEntryRequestDTO.from(accountEntryInput, addedBy))

  override fun subscriptionSchedulesAwaitingApproval(status: ChargeStatus): List<SchedulerStateDto> =
    accountServiceClient.getSubscriptionsPendingApproval(status)

  override fun addApprovedSubscriptions(requestBody: List<ApproveChargeRequestDto>, approvedBy: String) =
    accountServiceClient.addApprovedSubscriptions(requestBody, approvedBy)

  override fun getNumberFailedCharges(memberId: String): NumberFailedChargesDto =
    accountServiceClient.getNumberFailedCharges(memberId)
}
