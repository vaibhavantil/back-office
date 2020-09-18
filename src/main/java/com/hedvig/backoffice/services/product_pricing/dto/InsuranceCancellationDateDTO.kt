package com.hedvig.backoffice.services.product_pricing.dto;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class InsuranceCancellationDateDTO {
  Long memberId;
  UUID insuranceId;
  Instant cancellationDate;
}
