package com.hedvig.backoffice.services.claims.dto;

import lombok.Data;

@Data
public class ClaimEvent extends ClaimBackOffice {

  private String type;
  private String text;
}
