package com.hedvig.backoffice.web;

import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.payments.PaymentService;
import com.hedvig.backoffice.services.payments.dto.PayoutMemberRequest;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/payout")
public class PayoutController {

  private final PaymentService paymentService;
  private final PersonnelService personnelService;

  @Autowired
  PayoutController(
    PaymentService paymentService,
    PersonnelService personnelService
  ) {
    this.personnelService = personnelService;
    this.paymentService = paymentService;
  }

  @PostMapping("/{memberId}")
  public ResponseEntity<UUID> payoutMember(
    @PathVariable String memberId,
    @RequestBody PayoutMemberRequest payoutMemberRequest,
    @AuthenticationPrincipal Principal principal
    ) throws AuthorizationException {
    val personnel = personnelService.getPersonnel(principal.getName());
    return paymentService.payoutMember(memberId, personnel.getEmail(), payoutMemberRequest);
  }
}
