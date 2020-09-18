package com.hedvig.backoffice.services.members.dto;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Value;

@Value
public class InsuranceCancellationDTO {
  Long memberId;
  UUID insuranceId;
  LocalDate cancellationDate;
}
