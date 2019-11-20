package com.hedvig.backoffice.services.itemPricing.dto;

import lombok.Value;
import java.util.List;
import com.hedvig.backoffice.services.itemPricing.dto.FilterDTO;

@Value
public class ItemSearchQueryDTO {
  String category;
  String query;
  List<FilterDTO> filters;
}
