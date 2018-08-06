package com.hedvig.backoffice.services.product_pricing.dto;

import javax.money.MonetaryAmount;
import lombok.Value;

@Value
public class MonthlySubscriptionDTO {
  String memberId;
  MonetaryAmount subscription;
}
