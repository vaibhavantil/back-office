package com.hedvig.backoffice.services.account;

import com.hedvig.backoffice.graphql.types.account.AccountEntryInput;
import com.hedvig.backoffice.services.account.dto.AccountDTO;
import com.hedvig.backoffice.services.account.dto.ApproveChargeRequestDto;
import com.hedvig.backoffice.services.account.dto.NumberFailedChargesDto;
import com.hedvig.backoffice.services.account.dto.SchedulerStateDto;

import java.util.List;

public interface AccountService {
  AccountDTO getAccount(String memberId);

  List<AccountDTO> batchFindCurrentBalances(List<String> memberIds);

  void addAccountEntry(String memberId, AccountEntryInput accountEntryInput, String addedBy);

  List<SchedulerStateDto> subscriptionSchedulesAwaitingApproval(ChargeStatus status);

  void addApprovedSubscriptions(List<ApproveChargeRequestDto> requestBody, String approvedBy);

  NumberFailedChargesDto getNumberFailedCharges(String memberId);
}
