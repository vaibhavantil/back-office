package com.hedvig.backoffice.services.claims.dto;

import lombok.Value;

@Value
public class EmployeeClaimRequestDTO {
  private String claimId;
  private boolean coveringEmployee;
}
