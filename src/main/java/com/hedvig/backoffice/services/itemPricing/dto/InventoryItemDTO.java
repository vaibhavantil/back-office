package com.hedvig.backoffice.services.itemPricing.dto;

import lombok.Value;
import java.math.BigDecimal;
import com.hedvig.backoffice.services.itemPricing.dto.FilterDTO;
import java.util.List;

@Value
public class InventoryItemDTO {
  String inventoryItemId;
  String claimId;
  String itemName;
  String categoryName;
  String categoryId;
  BigDecimal value;
  String source;
  BigDecimal upperRange;
  BigDecimal lowerRange;
  String itemId;
  List<FilterDTO> filters;
}
