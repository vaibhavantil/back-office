package com.hedvig.backoffice.services.claims.dto;

import lombok.Data;

@Data
public class ClaimNote extends ClaimBackOffice {
  private String text;
  private String fileURL;
  private String handlerReference; // Optional employee email
}
