package com.hedvig.backoffice.graphql.types.account;

import com.hedvig.backoffice.services.account.dto.AccountDTO;
import lombok.Value;

import javax.money.MonetaryAmount;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
public class Account {
  String id;
  MonetaryAmount currentBalance;
  MonetaryAmount totalBalance;
  List<AccountEntry> entries;

  public static Account from(AccountDTO account) {
    return new Account(
      account.getMemberId(),
      account.getCurrentBalance(),
      account.getTotalBalance(),
      account.getEntries().stream()
      .map(AccountEntry::from)
      .collect(toList())
    );
  }
}
