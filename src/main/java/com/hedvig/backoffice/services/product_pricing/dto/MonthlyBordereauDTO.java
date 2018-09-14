package com.hedvig.backoffice.services.product_pricing.dto;

import javax.money.MonetaryAmount;
import lombok.Value;

@Value
public class MonthlyBordereauDTO {

  private String memberId;
  private MonetaryAmount expectedSubscription;
  private MonetaryAmount earnedSubscription;
  private String productType;

}
