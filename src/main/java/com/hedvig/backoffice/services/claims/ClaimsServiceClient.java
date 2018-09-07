package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.claims.dto.Claim;
import com.hedvig.backoffice.services.claims.dto.ClaimData;
import com.hedvig.backoffice.services.claims.dto.ClaimNote;
import com.hedvig.backoffice.services.claims.dto.ClaimPayment;
import com.hedvig.backoffice.services.claims.dto.ClaimReserveUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimStateUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimType;
import com.hedvig.backoffice.services.claims.dto.ClaimTypeUpdate;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
    name = "claims-service",
    url = "${claims.baseUrl}",
    configuration = FeignConfig.class,
    fallback = ClaimsServiceClientFallback.class)
public interface ClaimsServiceClient {

  @GetMapping("/_/claims/stat")
  Map<String, Long> statistics(@RequestHeader("Authorization") String token);

  @GetMapping("/_/claims/listclaims/{userId}")
  List<Claim> listByUserId(
      @PathVariable("userId") String userId, @RequestHeader("Authorization") String token);

  @GetMapping("/_/claims/listclaims")
  List<Claim> list(@RequestHeader("Authorization") String token);

  @GetMapping("/_/claims/claim?claimID={id}")
  Claim find(@PathVariable("id") String id, @RequestHeader("Authorization") String token);

  @GetMapping("/_/claims/claimTypes")
  List<ClaimType> types(@RequestHeader("Authorization") String token);

  @PostMapping("/_/claims/addpayment")
  void addPayment(@RequestBody ClaimPayment dto, @RequestHeader("Authorization") String token);

  @PostMapping("/_/claims/addnote")
  void addNote(@RequestBody ClaimNote dto, @RequestHeader("Authorization") String token);

  @PostMapping("/_/claims/adddataitem")
  void addDataItem(@RequestBody ClaimData data, @RequestHeader("Authorization") String token);

  @PostMapping("/_/claims/updatestate")
  void updateState(
      @RequestBody ClaimStateUpdate state, @RequestHeader("Authorization") String token);

  @PostMapping("/_/claims/updatereserve")
  void updateReserve(
      @RequestBody ClaimReserveUpdate reserve, @RequestHeader("Authorization") String token);

  @PostMapping("/_/claims/updatetype")
  void updateType(@RequestBody ClaimTypeUpdate type, @RequestHeader("Authorization") String token);
}
