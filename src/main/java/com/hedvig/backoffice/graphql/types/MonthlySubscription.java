package com.hedvig.backoffice.graphql.types;

import com.hedvig.backoffice.services.product_pricing.dto.MonthlySubscriptionDTO;
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

  public MonthlySubscription(MonthlySubscriptionDTO dto) {
    this.memberId = dto.getMemberId();
    this.amount = dto.getSubscription();
  }
}
