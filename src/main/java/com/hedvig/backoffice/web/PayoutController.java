package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.payments.PaymentService;
import com.hedvig.backoffice.services.payments.dto.PayoutMemberRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/payout")
public class PayoutController {

  private final PaymentService paymentService;

  @Autowired
  PayoutController(
    PaymentService paymentService
  ) {
    this.paymentService = paymentService;
  }

  @PostMapping("/{memberId}")
  public ResponseEntity<UUID> payoutMember(
    @PathVariable String memberId,
    @RequestBody PayoutMemberRequest payoutMemberRequest
    ) {
    return paymentService.payoutMember(memberId, payoutMemberRequest);
  }
}
