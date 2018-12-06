package com.hedvig.backoffice.services.claims.dto;

import java.util.UUID;
import javax.money.MonetaryAmount;
import lombok.Value;

@Value
public class ClaimPaymentRequest {

  UUID claimId;
  String memberId;
  MonetaryAmount amount;
  String handlerReference;
  boolean sanctionCheckSkipped;
  String paymentRequestNote;
  boolean exGratia;
}
