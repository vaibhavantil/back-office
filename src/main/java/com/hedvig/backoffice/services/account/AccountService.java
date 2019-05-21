package com.hedvig.backoffice.services.account;

import com.hedvig.backoffice.graphql.types.AccountEntryInput;
import com.hedvig.backoffice.services.account.dto.AccountDTO;
import com.hedvig.backoffice.services.account.dto.SchedulerStateDto;

import java.util.List;

public interface AccountService {
  AccountDTO getAccount(String memberId);

  void addAccountEntry(String memberId, AccountEntryInput accountEntryInput, String addedBy);

  List<SchedulerStateDto> subscriptionSchedulesAwaitingApproval(ChargeStatus chargeStatus);
}
