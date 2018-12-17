package com.hedvig.backoffice.services.claims.dto;

import java.util.UUID;
import javax.money.MonetaryAmount;
import lombok.Value;
import org.javamoney.moneta.Money;

@Value
public class ClaimPaymentRequest {

  private static String SEK = "SEK";

  UUID claimId;
  MonetaryAmount amount;
  MonetaryAmount deductible;
  String handlerReference;
  boolean sanctionCheckSkipped;
  String paymentRequestNote;
  boolean exGratia;

  public static ClaimPaymentRequest fromClaimPayment(ClaimPayment c) {
    return new ClaimPaymentRequest(UUID.fromString(c.claimID),
      Money.of(c.amount, SEK),
      Money.of(c.deductible, SEK),
      c.handlerReference,
      c.sanctionListSkipped,
      c.note,
      c.exGratia);
  }

}
