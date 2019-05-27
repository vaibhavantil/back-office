package com.hedvig.backoffice.services.account;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.account.dto.AccountBalanceDTO;
import com.hedvig.backoffice.services.account.dto.AccountDTO;
import com.hedvig.backoffice.services.account.dto.AccountEntryRequestDTO;
import com.hedvig.backoffice.services.account.dto.ApproveChargeRequestDto;
import com.hedvig.backoffice.services.account.dto.SchedulerStateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
  name = "account-service",
  url = "${accountService.baseUrl:account-service}",
  configuration = FeignConfig.class)
public interface AccountServiceClient {
  @PostMapping(path = "/_/accounts/{memberId}/entries")
  void addAccountEntry(
    @PathVariable("memberId") String memberId,
    @RequestBody AccountEntryRequestDTO requestBody
  );

  @GetMapping(path = "/_/accounts/{memberId}")
  AccountDTO getAccount(
    @PathVariable("memberId") String memberId
  );

  @PostMapping(path = "/_accounts/batchFind")
  List<AccountBalanceDTO> batchFindCurrentBalances(@RequestBody List<String> memberIds);

  @GetMapping(path = "/_/schedule/states")
  List<SchedulerStateDto> getSubscriptionsPendingApproval(
    @RequestParam("status") ChargeStatus status,
    @RequestParam("limit") Integer limit
  );

  @PostMapping(path = "/_/schedule/approvals")
  void addApprovedSubscriptions(
    @RequestBody List<ApproveChargeRequestDto> requestBody,
    @RequestParam("approvedBy") String approvedBy
  );
}
