package com.hedvig.backoffice.web.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hedvig.backoffice.services.product_pricing.dto.MonthlyBordereauDTO;

@JsonPropertyOrder(value = {"memberId", "expectedSubscription", "earnedSubscription",
  "productType"})
public class BordereauCSVDTO {

  public String memberId;
  public double expectedSubscription;
  public double earnedSubscription;
  public String productType;

  public BordereauCSVDTO(MonthlyBordereauDTO b) {
    this.memberId = b.getMemberId();
    this.expectedSubscription = b.getExpectedSubscription().getNumber().doubleValueExact();
    this.earnedSubscription = b.getEarnedSubscription().getNumber().doubleValueExact();
    this.productType = b.getProductType();
  }
}
