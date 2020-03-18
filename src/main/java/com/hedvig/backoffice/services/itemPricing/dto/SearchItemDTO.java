package com.hedvig.backoffice.services.itemPricing.dto;

import lombok.Value;

@Value
public class SearchItemDTO {
  String pricerunnerId;
  String name;
  String url;
}
