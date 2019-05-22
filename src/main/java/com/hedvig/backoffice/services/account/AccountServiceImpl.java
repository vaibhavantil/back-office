package com.hedvig.backoffice.services.account;

import com.hedvig.backoffice.graphql.types.AccountEntryInput;
import com.hedvig.backoffice.services.account.dto.AccountDTO;
import com.hedvig.backoffice.services.account.dto.AccountEntryRequestDTO;
import com.hedvig.backoffice.services.account.dto.ApproveChargeRequestDto;
import com.hedvig.backoffice.services.account.dto.SchedulerStateDto;
import org.javamoney.moneta.Money;

import java.util.Collections;
import java.util.List;

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

  @Override
  public List<SchedulerStateDto> subscriptionSchedulesAwaitingApproval(ChargeStatus chargeStatus) {
    return accountServiceClient.getSubscriptionsPendingApproval(chargeStatus);
  }

  @Override
  public void addApprovedSubscriptions(List<ApproveChargeRequestDto> requestBody) {
    accountServiceClient.addApprovedSubscriptions(requestBody);
  }
}
