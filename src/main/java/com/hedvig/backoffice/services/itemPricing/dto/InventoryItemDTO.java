package com.hedvig.backoffice.services.itemPricing.dto;

import lombok.Value;
import java.math.BigDecimal;

@Value
public class InventoryItemDTO {
  String inventoryItemId;
  String claimId;
  String itemName;
  String categoryName;
  BigDecimal value;
}
