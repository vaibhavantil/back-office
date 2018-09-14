package com.hedvig.backoffice.graphql.types;

import com.hedvig.backoffice.services.product_pricing.dto.MonthlyBordereauDTO;
import com.hedvig.backoffice.services.product_pricing.dto.ProductType;
import javax.money.MonetaryAmount;
import lombok.Value;

@Value
public class MonthlyBordereau {

  String memberId;
  MonetaryAmount expectedSubscription;
  MonetaryAmount earnedSubscription;
  ProductType productType;

  public MonthlyBordereau(MonthlyBordereauDTO dto) {
    this.memberId = dto.getMemberId();
    this.expectedSubscription = dto.getExpectedSubscription();
    this.earnedSubscription = dto.getEarnedSubscription();
    this.productType = ProductType.valueOf(dto.getProductType());
  }
}
