package com.hedvig.backoffice.services.claims.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ClaimBackOffice {

  protected String id;

  protected String claimID;

  protected LocalDateTime date;

  protected String userId;
}
