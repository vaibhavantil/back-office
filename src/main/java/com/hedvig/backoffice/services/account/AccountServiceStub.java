package com.hedvig.backoffice.services.account;

import com.hedvig.backoffice.graphql.types.AccountEntryInput;
import com.hedvig.backoffice.services.account.dto.*;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountServiceStub implements AccountService {

  private List<AccountEntryDTO> entries = new ArrayList<>();

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
        "system"
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
        "system"
      )
    );
  }

  @Override
  public AccountDTO getAccount(String memberId) {
    final BigDecimal currentBalance = entries.stream()
      .filter(accountEntry -> !YearMonth.from(accountEntry.getFromDate()).atEndOfMonth().isAfter(LocalDate.now().plusDays(1)))
      .map(AccountEntryDTO::getAmount)
      .map(amount -> amount.getNumber().numberValueExact(BigDecimal.class))
      .reduce(BigDecimal.ZERO, BigDecimal::add);
    final BigDecimal totalBalance = entries.stream()
      .map(AccountEntryDTO::getAmount)
      .map(amount -> amount.getNumber().numberValueExact(BigDecimal.class))
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    return new AccountDTO(
      memberId,
      Money.of(currentBalance, "SEK"),
      Money.of(totalBalance, "SEK"),
      entries
    );
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
      addedBy
    );
    entries.add(newAccountEntry);
  }

  @Override
  public List<SchedulerStateDto> subscriptionSchedulesAwaitingApproval(ChargeStatus chargeStatus) {
    List<SchedulerStateDto> subscriptionsPendingApproval = new ArrayList<>();
    subscriptionsPendingApproval.add(new SchedulerStateDto(
      UUID.randomUUID(),
      "1223",
      chargeStatus,
      "admin1",
      Instant.now(),
      null,
      null
      )
    );

    subscriptionsPendingApproval.add(new SchedulerStateDto(
        UUID.randomUUID(),
        "321",
        chargeStatus,
        "admin2",
        Instant.now(),
        null,
        null
      )
    );
    return subscriptionsPendingApproval;
  }

  @Override
  public void addApprovedSubscriptions(List<ApproveChargeRequestDto> requestBody) {

  }
}

