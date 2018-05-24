package com.hedvig.backoffice.services.members.dto;

import lombok.Value;
import java.time.LocalDate;

@Value
public class InsuranceCancellationDTO {
    LocalDate cancellationDate;
}
