package com.hedvig.backoffice.services.account;

import com.hedvig.backoffice.graphql.types.AccountEntryInput;
import com.hedvig.backoffice.services.account.dto.AccountDTO;
import com.hedvig.backoffice.services.account.dto.AccountEntryRequestDTO;
import org.javamoney.moneta.Money;

import java.util.Collections;

public class AccountServiceImpl implements AccountService {
  private final AccountServiceClient accountServiceClient;

  public AccountServiceImpl(final AccountServiceClient accountServiceClient) {
    this.accountServiceClient = accountServiceClient;
  }

  @Override
  public AccountDTO getAccount(String memberId) {
    return accountServiceClient.getAccount(memberId);
  }

  @Override
  public void addAccountEntry(String memberId, AccountEntryInput accountEntryInput, String addedBy) {
    accountServiceClient.addAccountEntry(memberId, AccountEntryRequestDTO.from(accountEntryInput, addedBy));
  }
}
