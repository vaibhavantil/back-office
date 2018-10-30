package com.hedvig.backoffice.graphql.types;

import javax.money.MonetaryAmount;
import lombok.Value;

@Value
public class ClaimPaymentInput {
  MonetaryAmount amount;
  String note;
  ClaimPaymentType type;
  Boolean exGratia;
}
