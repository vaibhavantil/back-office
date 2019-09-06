package com.hedvig.backoffice.services.members.dto;

import lombok.Value;

import javax.money.MonetaryAmount;

@Value
public class PaymentDefaultDTO {
  Integer year;
  Integer week;
  String paymentDefaultType;
  String paymentDefaultTypeText;
  MonetaryAmount amount;
  String caseId;
  String claimant;
}

