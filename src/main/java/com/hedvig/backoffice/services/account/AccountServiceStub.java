package com.hedvig.backoffice.services.account;

import com.hedvig.backoffice.graphql.types.AccountEntryInput;
import com.hedvig.backoffice.services.account.dto.AccountDTO;
import com.hedvig.backoffice.services.account.dto.AccountEntryDTO;
import com.hedvig.backoffice.services.account.dto.AccountEntryType;
import org.javamoney.moneta.Money;

import java.math.BigDecimal;
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
        AccountEntryType.FEE,
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
    final BigDecimal currentMonthsBalance = entries.stream()
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
      Money.of(currentMonthsBalance, "SEK"),
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
}
