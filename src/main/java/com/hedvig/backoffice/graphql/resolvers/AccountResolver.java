package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.graphql.types.Account;
import com.hedvig.backoffice.graphql.types.AccountEntry;
import com.hedvig.backoffice.services.account.AccountService;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class AccountResolver implements GraphQLResolver<Account> {
  private final AccountService accountService;

  public AccountResolver(final AccountService accountService) {
    this.accountService = accountService;
  }

  List<AccountEntry> getEntries(final String memberId) {
    return accountService.getAccount(memberId)
      .getEntries()
      .stream()
      .map(AccountEntry::from)
      .collect(toList());
  }
}
