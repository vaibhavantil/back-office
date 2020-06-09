package com.hedvig.backoffice.services.claims.dto;

import com.hedvig.backoffice.services.claims.ClaimState;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

//@Data Makes private fields accessible in Java, it is not Kotlin-compatible
@Data
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
public class Claim extends ClaimBackOffice {

  public String audioURL;
  public ClaimState state;
  public BigDecimal reserve;
  public String type;
  public ClaimSource claimSource;

  public List<ClaimNote> notes;
  public List<ClaimTranscription> transcriptions;
  public List<ClaimPayment> payments;
  public List<ClaimAsset> assets;
  public List<ClaimEvent> events;
  public List<ClaimData> data;

  private boolean coveringEmployee;
  public List<ClaimFileDTO> claimFiles = new ArrayList<>();
}
