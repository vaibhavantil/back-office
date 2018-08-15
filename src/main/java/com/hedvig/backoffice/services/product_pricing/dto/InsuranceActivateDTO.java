package com.hedvig.backoffice.services.product_pricing.dto;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Value;

@Value
public class InsuranceActivateDTO {
  UUID insuranceId;
  LocalDate activationDate;
}
