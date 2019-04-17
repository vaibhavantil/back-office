package com.hedvig.backoffice.services.payments.dto;

import lombok.Value;

import javax.money.MonetaryAmount;

@Value
public class PayoutRequest {
  MonetaryAmount amount;
  boolean sanctionBypassed;
}
