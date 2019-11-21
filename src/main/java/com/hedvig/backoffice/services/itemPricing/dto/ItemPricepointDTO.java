package com.hedvig.backoffice.services.itemPricing.dto;

import lombok.Value;
import java.math.BigDecimal;

@Value
public class ItemPricepointDTO {
  String id;
  String itemId;
  String date;

  BigDecimal lower;
  BigDecimal mean;
  BigDecimal upper;
}
