package com.hedvig.backoffice.services.itemPricing.dto;

import lombok.Value;
import java.util.List;

@Value
public class ItemCategoryDTO {
  String primary;
  List<String> secondaries;
}
