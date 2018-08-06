package com.hedvig.backoffice.services.claims.dto;

import lombok.Data;

@Data
public class ClaimData extends ClaimBackOffice {

  private String type;
  private String name;
  private String title;
  private Boolean received;
  private String value;
}
