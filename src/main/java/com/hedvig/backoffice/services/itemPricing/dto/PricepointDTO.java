package com.hedvig.backoffice.services.itemPricing.dto;

import lombok.Value;
import java.util.List;

@Value
public class PricepointDTO {
  String _id;
  String itemId;
  String date;

  double lower;
  double mean;
  double upper;
}
