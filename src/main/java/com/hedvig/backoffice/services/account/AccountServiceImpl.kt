package com.hedvig.backoffice.services.account

import com.hedvig.backoffice.graphql.types.account.AccountEntryInput
import com.hedvig.backoffice.graphql.types.account.MonthlyEntryInput
import com.hedvig.backoffice.services.account.dto.*
import org.springframework.http.HttpStatus
import java.util.*

class AccountServiceImpl(private val accountServiceClient: AccountServiceClient) : AccountService {

    override fun batchFindCurrentAccounts(memberIds: List<String>): List<AccountDTO?> =
        accountServiceClient.batchFindCurrentAccounts(memberIds)

    override fun addAccountEntry(memberId: String, accountEntryInput: AccountEntryInput, addedBy: String) =
        accountServiceClient.addAccountEntry(memberId, AccountEntryRequestDTO.from(accountEntryInput, addedBy))

    override fun addMonthlyEntry(memberId: String, monthlyEntryInput: MonthlyEntryInput, addedBy: String) =
        accountServiceClient.addMonthlyEntry(memberId, AddMonthlyEntryRequest.from(monthlyEntryInput), addedBy)

    override fun subscriptionSchedulesAwaitingApproval(status: ChargeStatus): List<SchedulerStateDto> =
        accountServiceClient.getSubscriptionsPendingApproval(status)

    override fun addApprovedSubscriptions(requestBody: List<ApproveChargeRequestDto>, approvedBy: String) =
        accountServiceClient.addApprovedSubscriptions(requestBody, approvedBy)

    override fun getNumberFailedCharges(memberId: String): NumberFailedChargesDto? {
        val response = accountServiceClient.getNumberFailedCharges(memberId)
        if (response.statusCode == HttpStatus.NOT_FOUND) {
            return null
        }
        return response.body
    }


    override fun backfillSubscriptions(memberId: String, backfilledBy: String) =
        accountServiceClient.backfillSubscriptions(memberId, backfilledBy)

    override fun getMonthlyEntriesForMember(memberId: String): MutableList<MonthlyEntryDto> =
        accountServiceClient.getMonthlyEntriesForMember(memberId)

    override fun removeMonthlyEntry(id: UUID, removedBy: String) =
        accountServiceClient.removeMonthlyEntry(id, removedBy)
}
