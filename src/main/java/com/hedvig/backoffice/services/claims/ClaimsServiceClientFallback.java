package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.claims.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ClaimsServiceClientFallback implements ClaimsServiceClient {

  private static Logger log = LoggerFactory.getLogger(ClaimsServiceClientFallback.class);

  @Override
  public Map<String, Long> statistics(String token) {
    log.error("request to claim-service failed");

    Map<String, Long> stat = new HashMap<>();
    for (ClaimState state : ClaimState.values()) {
      stat.put(state.name(), 0L);
    }

    return stat;
  }

  @Override
  public List<Claim> listByUserId(String userId, String token) {
    log.error("request to claim-service failed");
    return new ArrayList<>();
  }

  @Override
  public List<Claim> list(String token) {
    log.error("request to claim-service failed");
    return new ArrayList<>();
  }

  @Override
  public ClaimSearchResultDTO search(Integer page, Integer pageSize, ClaimSortColumn sortBy,
                                     Sort.Direction sortDirection, String token) {
    log.error("request to claim-service failed");
    return new ClaimSearchResultDTO(new ArrayList<>(), null, null);
  }

  @Override
  public Claim find(String id, String token) {
    log.error("request to claim-service failed");
    return null;
  }

  @Override
  public List<ClaimType> types(String token) {
    log.error("request to claim-service failed");
    return new ArrayList<>();
  }

  @Override
  public void addPayment(ClaimPayment dto, String token) {
    log.error("request to claim-service failed");
  }

  @Override
  public void addNote(ClaimNote dto, String token) {
    log.error("request to claim-service failed");
  }

  @Override
  public void addDataItem(ClaimData data, String token) {
    log.error("request to claim-service failed");
  }

  @Override
  public void updateState(ClaimStateUpdate state, String token) {
    log.error("request to claim-service failed");
  }

  @Override
  public void updateReserve(ClaimReserveUpdate reserve, String token) {
    log.error("request to claim-service failed");
  }

  @Override
  public void updateType(ClaimTypeUpdate type, String token) {
    log.error("request to claim-service failed");
  }

  @Override
  public List<Claim> getClaimsByIds(ClaimsByIdsDto dto) {
    log.error("request to claim-service failed");
    return null;
  }

  @Override
  public void addAutomaticPayment(String id, ClaimPaymentRequest dto) {
    log.error("request to claim-service failed");
  }

  @Override
  public CreateBackofficeClaimResponseDTO createClaim(CreateBackofficeClaimDTO req, String token) {
    log.error("request to claim-service failed");
    return null;
  }

  @Override
  public void markEmployeeClaim(EmployeeClaimRequestDTO dto, String token) {
    log.error("request to claim.service failed");
  }

  @Override
  public ResponseEntity<ClaimsFilesUploadDTO> allClaimsFiles(String claimId) {
    log.error("request to claim-service failed");
    return null;
  }

  @Override
  public ResponseEntity<ClaimFileDTO> claimFileById(String claimFileId) {
    log.error("request to claim-service failed");
    return null;
  }

  @Override
  public ResponseEntity<Void> uploadClaimsFiles(ClaimsFilesUploadDTO dto) {
    log.error("request to claim-service failed");
    return null;
  }

  @Override
  public ResponseEntity<Void> markClaimFileAsDeleted(String claimId, String claimFileId, MarkClaimFileAsDeletedDTO deletedBy) {
    log.error("request to claim-service failed");
    return null;
  }

  @Override
  public ResponseEntity<Void> setClaimFileCategory(String claimId, String claimFileId, ClaimFileCategoryDTO dto) {
    return null;
  }
}
