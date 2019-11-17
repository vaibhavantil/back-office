package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.claims.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "claims-service", url = "${claims.baseUrl}", configuration = FeignConfig.class,
  fallback = ClaimsServiceClientFallback.class)
public interface ClaimsServiceClient {

  @GetMapping("/_/claims/stat")
  Map<String, Long> statistics(@RequestHeader("Authorization") String token);

  @GetMapping("/_/claims/listclaims/{userId}")
  List<Claim> listByUserId(@PathVariable("userId") String userId,
                           @RequestHeader("Authorization") String token);

  @GetMapping("/_/claims/listclaims")
  List<Claim> list(@RequestHeader("Authorization") String token);

  @GetMapping("/_/claims/search")
  ClaimSearchResultDTO search(@RequestParam("page") Integer page,
                              @RequestParam("pageSize") Integer pageSize, @RequestParam("sortBy") ClaimSortColumn sortBy,
                              @RequestParam("sortDirection") Sort.Direction sortDirection,
                              @RequestHeader("Authorization") String token);

  @GetMapping("/_/claims/claim?claimID={id}")
  Claim find(@PathVariable("id") String id, @RequestHeader("Authorization") String token);

  @GetMapping("/_/claims/claimTypes")
  List<ClaimType> types(@RequestHeader("Authorization") String token);

  @PostMapping("/_/claims/addpayment")
  void addPayment(@RequestBody ClaimPayment dto, @RequestHeader("Authorization") String token);

  @PostMapping("/_/claims/{memberId}/addAutomaticPayment")
  void addAutomaticPayment(@PathVariable("memberId") String memberId, @RequestBody ClaimPaymentRequest dto);

  @PostMapping("/_/claims/addnote")
  void addNote(@RequestBody ClaimNote dto, @RequestHeader("Authorization") String token);

  @PostMapping("/_/claims/adddataitem")
  void addDataItem(@RequestBody ClaimData data, @RequestHeader("Authorization") String token);

  @PostMapping("/_/claims/updatestate")
  void updateState(@RequestBody ClaimStateUpdate state,
                   @RequestHeader("Authorization") String token);

  @PostMapping("/_/claims/updatereserve")
  void updateReserve(@RequestBody ClaimReserveUpdate reserve,
                     @RequestHeader("Authorization") String token);

  @PostMapping("/_/claims/updatetype")
  void updateType(@RequestBody ClaimTypeUpdate type, @RequestHeader("Authorization") String token);

  @PostMapping("/_/claims/many")
  List<Claim> getClaimsByIds(@RequestBody ClaimsByIdsDto dto);

  @PostMapping("/_/claims/createFromBackOffice")
  CreateBackofficeClaimResponseDTO createClaim(@RequestBody CreateBackofficeClaimDTO req, @RequestHeader("Authorization") String token);

  @PostMapping("/_/claims/employee")
  void markEmployeeClaim(@RequestBody EmployeeClaimRequestDTO req, @RequestHeader("Authorization") String token);

  @GetMapping("/_/claims/{claimId}/claimFiles")
  ResponseEntity<ClaimsFilesUploadDTO> allClaimsFiles(@PathVariable String claimId);

  @GetMapping("/_/claims/claimFile/{claimFileId}")
  ResponseEntity<ClaimFileDTO> claimFileById(@PathVariable String claimFileId);

  @PostMapping("/_/claims/claimFiles")
  ResponseEntity<Void> uploadClaimsFiles(@RequestBody ClaimsFilesUploadDTO dto);

  @PostMapping("/_/claims/{claimId}/markAsDeleted/{claimFileId}")
  ResponseEntity<Void> markClaimFileAsDeleted(
    @PathVariable String claimId, @PathVariable String claimFileId,
    @RequestBody MarkClaimFileAsDeletedDTO deletedBy);

  @PostMapping("/_/claims/{claimId}setClaimFileCategory/{claimFileId}")
  ResponseEntity<Void> setClaimFileCategory(
    @PathVariable String claimId, @PathVariable String claimFileId,
    @RequestBody ClaimFileCategoryDTO dto
  );
}
