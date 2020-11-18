package com.hedvig.backoffice.services.account

import com.hedvig.backoffice.graphql.types.account.AccountEntryInput
import com.hedvig.backoffice.graphql.types.account.MonthlyEntryInput
import com.hedvig.backoffice.services.account.dto.*
import java.util.*

interface AccountService {
    fun batchFindCurrentAccounts(memberIds: List<String>): List<AccountDTO?>
    fun addAccountEntry(memberId: String, accountEntryInput: AccountEntryInput, addedBy: String)
    fun addMonthlyEntry(memberId: String, monthlyEntryInput: MonthlyEntryInput, addedBy: String)
    fun backfillSubscriptions(memberId: String, backfilledBy: String)
    fun subscriptionSchedulesAwaitingApproval(status: ChargeStatus): List<SchedulerStateDto>
    fun addApprovedSubscriptions(requestBody: List<ApproveChargeRequestDto>, approvedBy: String)
    fun getNumberFailedCharges(memberId: String): NumberFailedChargesDto
    fun getMonthlyEntriesForMember(memberId: String): List<MonthlyEntryDto>
    fun removeMonthlyEntry(id: UUID, removedBy: String)
}
