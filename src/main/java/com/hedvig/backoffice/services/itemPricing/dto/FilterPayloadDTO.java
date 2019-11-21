package com.hedvig.backoffice.services.itemPricing.dto;

import lombok.Value;
import java.util.List;
import com.hedvig.backoffice.services.itemPricing.dto.FilterDTO;

@Value
public class FilterPayloadDTO {
  List<FilterDTO> filters;
  String inventoryItemId;

}
