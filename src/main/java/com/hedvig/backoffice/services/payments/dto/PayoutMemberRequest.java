package com.hedvig.backoffice.services.payments.dto;

import lombok.Value;

import javax.money.MonetaryAmount;

@Value
public class PayoutMemberRequest {
  MonetaryAmount amount;
  PayoutCategory category;
  String referenceId;
  String note;
}
