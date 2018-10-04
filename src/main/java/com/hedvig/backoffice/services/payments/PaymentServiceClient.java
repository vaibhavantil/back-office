package com.hedvig.backoffice.services.payments;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.payments.dto.ChargeRequestDTO;
import com.hedvig.backoffice.services.payments.dto.DirectDebitStatusDTO;
import com.hedvig.backoffice.services.payments.dto.PaymentMemberDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "payment-service",
    url = "${paymentService.baseUrl}",
    configuration = FeignConfig.class)
public interface PaymentServiceClient {
  @GetMapping("/_/members/directDebitStatus/{memberIds}")
  List<DirectDebitStatusDTO> getDirectDebitStatuses(
      @PathVariable("memberIds") List<String> memberIds);

  @GetMapping("/_/members/{memberId}/transactions")
  PaymentMemberDTO getTransactions(@PathVariable("memberId") String memberId);

  @PostMapping("/_/members/{memberId}/charge")
  void chargeMember(
      @PathVariable("memberId") String memberId, @RequestBody ChargeRequestDTO chargeRequestDto);
}
