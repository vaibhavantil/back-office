package com.hedvig.backoffice.graphql.types;

import lombok.Value;

import javax.money.MonetaryAmount;

@Value
public class PaymentDefault {
  Integer year;
  Integer week;
  String paymentDefaultType;
  String paymentDefaultTypeText;
  MonetaryAmount amount;
  String caseId;
  String claimant;
}
