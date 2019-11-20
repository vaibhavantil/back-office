package com.hedvig.backoffice.services.itemPricing.dto;

import com.hedvig.backoffice.services.itemPricing.dto.FilterSuggestionDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemDTO;

import lombok.Value;
import java.util.List;

@Value
public class ItemSearchDTO {
  List<ItemDTO> products;
  List<FilterSuggestionDTO> suggestions;
}
