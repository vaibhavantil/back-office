package com.hedvig.backoffice.graphql.dataloaders;

import com.hedvig.backoffice.graphql.types.Account;
import com.hedvig.backoffice.services.account.AccountService;
import org.dataloader.BatchLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.stream.Collectors.toList;

@Component
public class AccountBatchLoader implements BatchLoader<String, Account> {
  private final AccountService accountService;

  public AccountBatchLoader(final AccountService accountService) {
    this.accountService = accountService;
  }

  @Override
  public CompletionStage<List<Account>> load(List<String> memberIds) {
    return CompletableFuture.supplyAsync(() -> accountService.batchFindCurrentBalances(memberIds)
      .stream()
      .map(Account::from)
      .collect(toList()));
  }
}
