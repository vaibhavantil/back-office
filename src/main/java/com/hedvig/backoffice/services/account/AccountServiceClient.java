package com.hedvig.backoffice.services.account;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.account.dto.AccountDTO;
import com.hedvig.backoffice.services.account.dto.AccountEntryRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
}
