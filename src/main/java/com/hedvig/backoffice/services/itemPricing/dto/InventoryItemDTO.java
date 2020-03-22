package com.hedvig.backoffice.services.itemPricing.dto;

import lombok.Value;
import java.math.BigDecimal;
import java.time.LocalDate;

@Value
public class InventoryItemDTO {
  String inventoryItemId;
  String claimId;
  String itemName;
  String categoryName;
  BigDecimal value;
  LocalDate purchaseDate;
}
