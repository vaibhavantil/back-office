package com.hedvig.backoffice.services.account;

import com.hedvig.backoffice.services.account.dto.Account;

public interface AccountService {
  Account getAccount(String memberId);
}
