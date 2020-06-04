package com.hedvig.backoffice.services.claims.dto;

import com.hedvig.backoffice.services.claims.ClaimState;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
public class Claim extends ClaimBackOffice {

  private String audioURL;
  private ClaimState state;
  private BigDecimal reserve;
  private String type;
  private ClaimSource claimSource;

  private List<ClaimNote> notes;
  private List<ClaimTranscription> transcriptions;
  private List<ClaimPayment> payments;
  private List<ClaimAsset> assets;
  private List<ClaimEvent> events;
  private List<ClaimData> data;

  private boolean coveringEmployee;
  public List<ClaimFileDTO> claimFiles = new ArrayList<>();
}
