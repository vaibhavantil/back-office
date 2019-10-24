package com.hedvig.backoffice.graphql.dataloaders;

import com.hedvig.backoffice.graphql.types.account.Account;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.springframework.stereotype.Component;

@Component
public class AccountLoader extends DataLoader<String, Account> {
  public AccountLoader(AccountBatchLoader accountBatchLoader) {
    super(accountBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
  }
}
