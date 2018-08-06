package com.hedvig.backoffice.services.claims.dto;

import com.hedvig.backoffice.services.claims.ClaimState;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class Claim extends ClaimBackOffice {

  private String audioURL;
  private ClaimState state;
  private BigDecimal reserve;
  private String type;

  private List<ClaimNote> notes;
  private List<ClaimPayment> payments;
  private List<ClaimAsset> assets;
  private List<ClaimEvent> events;
  private List<ClaimData> data;

  private LocalDateTime registrationDate;
}
