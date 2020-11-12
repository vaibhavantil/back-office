package com.hedvig.backoffice.services.account

import com.hedvig.backoffice.graphql.types.account.AccountEntryInput
import com.hedvig.backoffice.graphql.types.account.MonthlyEntryInput
import com.hedvig.backoffice.services.account.dto.*
import org.javamoney.moneta.Money
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.util.*
import java.util.stream.Collectors
import javax.money.MonetaryAmount

class AccountServiceStub : AccountService {
    private val entries: MutableList<AccountEntryDTO> = ArrayList()
    private val subscription = BigDecimal.valueOf(99)
    private val discount = BigDecimal.valueOf(10)
    private val chargeEstimation: AccountChargeEstimationDTO
    private val currentMonthsBalance = calculateCurrentMonthsBalance()
    private val totalBalance = calculateTotalBalance()


    init {
        entries.add(
            AccountEntryDTO(
                UUID.randomUUID(),
                LocalDate.now(),
                Money.of(BigDecimal.TEN, "SEK"),
                AccountEntryType.CAMPAIGN,
                "Member",
                "123123",
                "Title",
                "Valborg campaign 2019",
                "system",
                null,
                null
            )
        )
        entries.add(
            AccountEntryDTO(
                UUID.randomUUID(),
                LocalDate.now(),
                Money.of(BigDecimal.TEN.negate(), "SEK"),
                AccountEntryType.SUBSCRIPTION,
                "Product",
                UUID.randomUUID().toString(),
                "Monthly insurance fee",
                null,
                "system",
                null,
                null
            )
        )
        chargeEstimation = AccountChargeEstimationDTO(
            Money.of(subscription, "SEK"),
            Money.of(discount, "SEK"),
            Money.of(currentMonthsBalance.add(subscription).subtract(discount), "SEK"),
            java.util.List.of("referral code")
        )
    }

    override fun batchFindCurrentAccounts(memberIds: List<String>): List<AccountDTO?> {
        return memberIds.stream()
            .map { memberId: String? ->
                AccountDTO(
                    memberId!!,
                    Money.of(calculateCurrentMonthsBalance(), "SEK"),
                    Money.of(calculateTotalBalance(), "SEK"),
                    entries,
                    chargeEstimation
                )
            }
            .collect(Collectors.toList())
    }

    override fun addAccountEntry(memberId: String, accountEntryInput: AccountEntryInput, addedBy: String) {
        val newAccountEntry = AccountEntryDTO(
            UUID.randomUUID(),
            accountEntryInput.fromDate,
            accountEntryInput.amount,
            accountEntryInput.type,
            accountEntryInput.source,
            accountEntryInput.reference,
            accountEntryInput.title,
            accountEntryInput.comment,
            addedBy,
            null,
            null
        )
        entries.add(newAccountEntry)
    }

    override fun backfillSubscriptions(memberId: String, backfilledBy: String) {
        //TODO: enter stub info
    }

    override fun subscriptionSchedulesAwaitingApproval(chargeStatus: ChargeStatus): List<SchedulerStateDto> {
        val subscriptionsPendingApproval: MutableList<SchedulerStateDto> = ArrayList()
        subscriptionsPendingApproval.add(SchedulerStateDto(
            UUID.randomUUID(),
            "338786454",
            ChargeStatus.APPROVED_FOR_CHARGE,
            "admin1",
            Instant.now(),
            null,
            null
        )
        )
        subscriptionsPendingApproval.add(SchedulerStateDto(
            UUID.randomUUID(),
            "477408051",
            ChargeStatus.SUBSCRIPTION_SCHEDULED_AND_WAITING_FOR_APPROVAL,
            "admin2",
            Instant.now(),
            null,
            null
        )
        )
        return subscriptionsPendingApproval
    }

    override fun addApprovedSubscriptions(requestBody: List<ApproveChargeRequestDto>, approvedBy: String) {}
    override fun getNumberFailedCharges(memberId: String): NumberFailedChargesDto {
        val numberFailedCharges = (10 * Math.random()).toInt()
        return NumberFailedChargesDto(
            memberId,
            numberFailedCharges,
            if (numberFailedCharges > 0) Instant.now() else null
        )
    }

    private fun calculateCurrentMonthsBalance(): BigDecimal {
        return entries.stream()
            .filter { (_, fromDate) -> !YearMonth.from(fromDate).atEndOfMonth().isAfter(LocalDate.now().plusDays(1)) }
            .map(AccountEntryDTO::amount)
            .map { amount: MonetaryAmount -> amount.number.numberValueExact(BigDecimal::class.java) }
            .reduce(BigDecimal.ZERO) { obj: BigDecimal, augend: BigDecimal? -> obj.add(augend) }
    }

    private fun calculateTotalBalance(): BigDecimal {
        return entries.stream()
            .map(AccountEntryDTO::amount)
            .map { amount: MonetaryAmount -> amount.number.numberValueExact(BigDecimal::class.java) }
            .reduce(BigDecimal.ZERO) { obj: BigDecimal, augend: BigDecimal? -> obj.add(augend) }
    }

    override fun getMonthlyEntriesForMember(memberId: String): List<MonthlyEntryDto> {
        return emptyList()
    }

    override fun addMonthlyEntry(memberId: String, monthlyEntryInput: MonthlyEntryInput, addedBy: String) {
        // noop
    }

    override fun removeMonthlyEntry(id: UUID, removedBy: String) {
        // noop
    }
}
