package com.hedvig.backoffice.services.claims.dto;

import lombok.Data;

@Data
public class ClaimDeductibleUpdate {
  private Double amount;
  private  String claimID;
}
