package com.hedvig.backoffice.services.claims.dto;

import java.util.UUID;
import javax.money.MonetaryAmount;
import lombok.Value;
import org.javamoney.moneta.Money;

@Value
public class ClaimPaymentRequest {

  private static String SEK = "SEK";

  UUID claimId;
  String memberId;
  MonetaryAmount amount;
  String handlerReference;
  boolean sanctionCheckSkipped;
  String paymentRequestNote;
  boolean exGratia;

  public static ClaimPaymentRequest fromClaimPayment(ClaimPayment c) {
    return new ClaimPaymentRequest(UUID.fromString(c.claimID), c.userId, Money.of(c.amount, SEK),
      c.handlerReference, c.sanctionListSkipped, c.paymentNote, c.exGratia);
  }

}
