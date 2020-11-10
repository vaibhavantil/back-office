package com.hedvig.backoffice.graphql.types;

import javax.money.MonetaryAmount;
import lombok.Value;

@Value
public class MonthlySubscription {
  String memberId;
  MonetaryAmount amount;

  public MonthlySubscription(String memberId, MonetaryAmount amount) {
    this.memberId = memberId;
    this.amount = amount;
  }
}
