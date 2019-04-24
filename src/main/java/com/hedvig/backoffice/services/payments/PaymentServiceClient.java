package com.hedvig.backoffice.services.payments;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.payments.dto.*;

import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
  name = "payment-service",
  url = "${paymentService.baseUrl}",
  configuration = FeignConfig.class)
public interface PaymentServiceClient {

  @GetMapping("/_/members/directDebitStatus/{memberIds}")
  List<DirectDebitStatusDTO> getDirectDebitStatuses(@PathVariable("memberIds") List<String> memberIds);

  @GetMapping("/_/members/{memberId}/transactions")
  PaymentMemberDTO getTransactions(@PathVariable("memberId") String memberId);

  @PostMapping("/_/members/{memberId}/charge")
  void chargeMember(
    @PathVariable("memberId") String memberId,
    @RequestBody ChargeRequestDTO chargeRequestDto
  );

  @GetMapping("/directDebit/status")
  ResponseEntity<DirectDebitStatusDTO> getDirectDebitStatusByMemberId(@RequestHeader("Hedvig.Token") String memberId);

  @GetMapping("/_/transactions/{transactionId}")
  ResponseEntity<TransactionDTO> getTransactionById(@PathVariable UUID transactionId);

  @PostMapping("/v2/_/members/{memberId}/payout")
  ResponseEntity<UUID> payoutMember(
    @PathVariable String memberId,
    @RequestParam(name="category") PayoutCategory category,
    @RequestParam(name="referenceId", required = false) String referenceId,
    @RequestParam(name="note", required = false) String note,
    @RequestBody PayoutRequest request
  );
}
