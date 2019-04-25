package com.hedvig.backoffice.services.account;

import com.hedvig.backoffice.graphql.types.AccountEntryInput;
import com.hedvig.backoffice.services.account.dto.AccountDTO;

public interface AccountService {
  AccountDTO getAccount(String memberId);

  void addAccountEntry(String memberId, AccountEntryInput accountEntryInput);
}
