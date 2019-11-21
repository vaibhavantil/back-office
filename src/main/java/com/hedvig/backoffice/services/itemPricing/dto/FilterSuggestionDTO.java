package com.hedvig.backoffice.services.itemPricing.dto;

import lombok.Value;
import java.util.List;

@Value
public class FilterSuggestionDTO {
  String name;
  List<String> items;
  List<String> others;
}
