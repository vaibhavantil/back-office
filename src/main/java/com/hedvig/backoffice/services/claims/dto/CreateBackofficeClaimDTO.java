package com.hedvig.backoffice.services.claims.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBackofficeClaimDTO {
  String memberId;
  Instant registrationDate;
  ClaimSource claimSource;
}
