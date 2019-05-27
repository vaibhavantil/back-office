package com.hedvig.backoffice.services.account;

import com.hedvig.backoffice.graphql.types.AccountEntryInput;
import com.hedvig.backoffice.services.account.dto.AccountBalanceDTO;
import com.hedvig.backoffice.services.account.dto.AccountDTO;
import com.hedvig.backoffice.services.account.dto.ApproveChargeRequestDto;
import com.hedvig.backoffice.services.account.dto.SchedulerStateDto;

import java.util.List;

public interface AccountService {
  AccountDTO getAccount(String memberId);

  List<AccountBalanceDTO> batchFindCurrentBalances(List<String> memberIds);

  void addAccountEntry(String memberId, AccountEntryInput accountEntryInput, String addedBy);

  List<SchedulerStateDto> subscriptionSchedulesAwaitingApproval(ChargeStatus status);

  void addApprovedSubscriptions(List<ApproveChargeRequestDto> requestBody, String approvedBy);
}
