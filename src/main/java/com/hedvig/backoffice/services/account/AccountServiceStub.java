package com.hedvig.backoffice.services.account;

import com.hedvig.backoffice.graphql.types.AccountEntryInput;
import com.hedvig.backoffice.services.account.dto.AccountDTO;
import com.hedvig.backoffice.services.account.dto.AccountEntryDTO;
import com.hedvig.backoffice.services.account.dto.AccountEntryType;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.time.LocalDate;
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
        Optional.of("Valborg campaign 2019")
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
        Optional.empty()
      )
    );
  }

  @Override
  public AccountDTO getAccount(String memberId) {
    final BigDecimal balance = entries.stream()
      .map(AccountEntryDTO::getAmount)
      .map(amount -> amount.getNumber().numberValueExact(BigDecimal.class))
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    return new AccountDTO(
      memberId,
      Money.of(balance, "SEK"),
      entries
    );
  }

  @Override
  public void addAccountEntry(String memberId, AccountEntryInput accountEntryInput) {
    final AccountEntryDTO newAccountEntry = new AccountEntryDTO(
      UUID.randomUUID(),
      accountEntryInput.getFromDate(),
      accountEntryInput.getAmount(),
      accountEntryInput.getType(),
      accountEntryInput.getSource(),
      accountEntryInput.getReference(),
      accountEntryInput.getTitle(),
      accountEntryInput.getComment()
    );

    entries.add(newAccountEntry);
  }
}
