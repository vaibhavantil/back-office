package com.hedvig.backoffice.web.dto;

import com.hedvig.backoffice.services.claims.dto.ClaimSource;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateClaimDTO {
  String memberId;
  LocalDateTime registrationDate;
  ClaimSource claimSource;
}
