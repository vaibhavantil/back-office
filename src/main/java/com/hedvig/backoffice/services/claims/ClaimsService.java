package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.claims.dto.*;
import java.io.IOException;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

public interface ClaimsService {

  List<Claim> list(String token);

  List<Claim> listByUserId(String userId, String token);

  Claim find(String id, String token);

  List<ClaimType> types(String token);

  ClaimSearchResultDTO search(Integer page, Integer pageSize, ClaimSortColumn sortBy,
                              Sort.Direction sortDirection, String token);

  ClaimPaymentResponse addPayment(String memberId, ClaimPayment dto, String token);

  void addNote(ClaimNote dto, String token);

  void addData(ClaimData data, String token);

  void changeState(ClaimStateUpdate state, String token);

  void changeReserve(ClaimReserveUpdate reserve, String token);

  void changeType(ClaimTypeUpdate type, String token);

  Map<String, Long> statistics(String token);

  long totalClaims(String token);

  List<Claim> getClaimsByIds(List<UUID> ids);

  UUID createClaim(CreateBackofficeClaimDTO claimData, String token);

  void markEmployeeClaim(EmployeeClaimRequestDTO dto, String token);

  ResponseEntity<ClaimsFilesUploadDTO> allClaimsFiles(UUID claimId);

  ResponseEntity<Void> uploadClaimsFiles(UUID claimId, MultipartFile[] claimFiles) throws IOException;

  void deleteClaimFile(UUID claimId, UUID claimFileId);
}
