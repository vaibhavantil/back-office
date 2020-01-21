package com.hedvig.backoffice.services.account;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.hedvig.backoffice.graphql.types.account.AccountEntryInput;
import com.hedvig.backoffice.services.account.dto.*;
import org.javamoney.moneta.Money;

import javax.annotation.concurrent.Immutable;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class AccountServiceStub implements AccountService {

  private List<AccountEntryDTO> entries = new ArrayList<>();
  private BigDecimal subscription =  BigDecimal.valueOf(99);
  private BigDecimal discount =  BigDecimal.valueOf(10);
  private AccountChargeEstimationDTO chargeEstimation;
  private BigDecimal currentMonthsBalance = calculateCurrentMonthsBalance();
  private BigDecimal totalBalance = calculateTotalBalance();

  public AccountServiceStub() {
    entries.add(
      new AccountEntryDTO(
        UUID.randomUUID(),
        LocalDate.now(),
        Money.of(50, "SEK"),
        AccountEntryType.CAMPAIGN,
        "Member",
        "123123",
        Optional.of("Title"),
        Optional.of("Valborg campaign 2019"),
        "system",
        Optional.empty(),
        Optional.empty()
      )
    );
    entries.add(
      new AccountEntryDTO(
        UUID.randomUUID(),
        LocalDate.now(),
        Money.of(-100, "SEK"),
        AccountEntryType.SUBSCRIPTION,
        "Product",
        UUID.randomUUID().toString(),
        Optional.of("Monthly insurance fee"),
        Optional.empty(),
        "system",
        Optional.empty(),
        Optional.empty()
      )
    );

    chargeEstimation = new AccountChargeEstimationDTO(
        Money.of(subscription, "SEK"),
        Money.of(discount, "SEK"),
        Money.of(currentMonthsBalance.add(subscription).subtract(discount), "SEK"),
        List.of("referral code")
      );
  }

  @Override
  public AccountDTO getAccount(String memberId) {
      return new AccountDTO(
      memberId,
      Money.of(currentMonthsBalance, "SEK"),
      Money.of(totalBalance, "SEK"),
      entries,
      chargeEstimation
    );
  }

  @Override
  public List<AccountDTO> batchFindCurrentBalances(final List<String> memberIds) {
    return memberIds.stream()
      .map(memberId -> new AccountDTO(
        memberId,
        Money.of(calculateCurrentMonthsBalance(), "SEK"),
        Money.of(calculateTotalBalance(), "SEK"),
        entries,
        chargeEstimation
      ))
      .collect(toList());
  }

  @Override
  public void addAccountEntry(String memberId, AccountEntryInput accountEntryInput, String addedBy) {
    final AccountEntryDTO newAccountEntry = new AccountEntryDTO(
      UUID.randomUUID(),
      accountEntryInput.getFromDate(),
      accountEntryInput.getAmount(),
      accountEntryInput.getType(),
      accountEntryInput.getSource(),
      accountEntryInput.getReference(),
      accountEntryInput.getTitle(),
      accountEntryInput.getComment(),
      addedBy,
      Optional.empty(),
      Optional.empty()
    );
    entries.add(newAccountEntry);
  }

  @Override
  public void backfillSubscriptions(String memberId, String backfilledBy) {
    //TODO: enter stub info
  }

  @Override
  public List<SchedulerStateDto> subscriptionSchedulesAwaitingApproval(ChargeStatus chargeStatus) {
    List<SchedulerStateDto> subscriptionsPendingApproval = new ArrayList<>();
    subscriptionsPendingApproval.add(new SchedulerStateDto(
      UUID.randomUUID(),
      "338786454",
      ChargeStatus.APPROVED_FOR_CHARGE,
      "admin1",
      Instant.now(),
      null,
      null
      )
    );

    subscriptionsPendingApproval.add(new SchedulerStateDto(
        UUID.randomUUID(),
        "477408051",
        ChargeStatus.SUBSCRIPTION_SCHEDULED_AND_WAITING_FOR_APPROVAL,
        "admin2",
        Instant.now(),
        null,
        null
      )
    );
    return subscriptionsPendingApproval;
  }

  @Override
  public void addApprovedSubscriptions(List<ApproveChargeRequestDto> requestBody, String approvedBy) {

  }

  @Override
  public NumberFailedChargesDto getNumberFailedCharges(String memberId) {
    int numberFailedCharges = (int)(10 * Math.random());
    return new NumberFailedChargesDto(
      memberId,
      numberFailedCharges,
      numberFailedCharges > 0 ? Instant.now() : null
    );
  }

  private BigDecimal calculateCurrentMonthsBalance() {
    return entries.stream()
      .filter(accountEntry -> !YearMonth.from(accountEntry.getFromDate()).atEndOfMonth().isAfter(LocalDate.now().plusDays(1)))
      .map(AccountEntryDTO::getAmount)
      .map(amount -> amount.getNumber().numberValueExact(BigDecimal.class))
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private BigDecimal calculateTotalBalance() {
    return entries.stream()
      .map(AccountEntryDTO::getAmount)
      .map(amount -> amount.getNumber().numberValueExact(BigDecimal.class))
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}

