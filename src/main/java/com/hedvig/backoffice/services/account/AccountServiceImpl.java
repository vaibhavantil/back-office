package com.hedvig.backoffice.services.account;

import com.hedvig.backoffice.services.account.dto.Account;
import com.hedvig.backoffice.services.account.dto.Entry;
import com.hedvig.backoffice.services.account.dto.EntryType;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountServiceImpl implements AccountService {

  @Override
  public Account getAccount(String memberId) {
    List<Entry> entries = new ArrayList<>();
    entries.add(
      new Entry(
        UUID.randomUUID(),
        LocalDate.now(),
        Money.of(50, "SEK"),
        EntryType.CAMPAIGN,
        "Member",
        "123123",
        Optional.of("Title"),
        Optional.of("Valborg campaign 2019")
      )
    );
    entries.add(
      new Entry(
        UUID.randomUUID(),
        LocalDate.now(),
        Money.of(-100, "SEK"),
        EntryType.FEE,
        "Product",
        UUID.randomUUID().toString(),
        Optional.of("Monthly insurance fee"),
        Optional.empty()
      )
    );
    return new Account(memberId, entries);
  }
}
