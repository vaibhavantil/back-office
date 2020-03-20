package com.hedvig.backoffice.services.itemPricing.dto;

import lombok.Value;
import java.util.List;
import java.math.BigDecimal;

@Value
public class SearchItemDTO {
  String name;
  String category;
  String url;
  List<String> pricerunnerId;
  BigDecimal price;

}
