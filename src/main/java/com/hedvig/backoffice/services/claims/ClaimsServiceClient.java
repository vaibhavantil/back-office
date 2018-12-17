package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.claims.dto.Claim;
import com.hedvig.backoffice.services.claims.dto.ClaimData;
import com.hedvig.backoffice.services.claims.dto.ClaimNote;
import com.hedvig.backoffice.services.claims.dto.ClaimPayment;
import com.hedvig.backoffice.services.claims.dto.ClaimPaymentRequest;
import com.hedvig.backoffice.services.claims.dto.ClaimReserveUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimSearchResultDTO;
import com.hedvig.backoffice.services.claims.dto.ClaimSortColumn;
import com.hedvig.backoffice.services.claims.dto.ClaimStateUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimType;
import com.hedvig.backoffice.services.claims.dto.ClaimTypeUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimsByIdsDto;
import java.util.List;
import java.util.Map;

import com.hedvig.backoffice.services.claims.dto.CreateBackofficeClaimDTO;
import com.hedvig.backoffice.services.claims.dto.CreateBackofficeClaimResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

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
}
