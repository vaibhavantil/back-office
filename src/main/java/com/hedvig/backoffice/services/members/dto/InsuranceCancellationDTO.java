package com.hedvig.backoffice.services.members.dto;

import java.util.UUID;
import lombok.Value;
import java.time.LocalDate;

@Value
public class InsuranceCancellationDTO {
    Long memberId;
    UUID insuranceId;
    LocalDate cancellationDate;
}
