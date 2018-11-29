package com.hedvig.backoffice.services.claims.dto;

import lombok.Value;

import java.util.UUID;

@Value
public class CreateBackofficeClaimResponseDTO {
  UUID claimId;
}
