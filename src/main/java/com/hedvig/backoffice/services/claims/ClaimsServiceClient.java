package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.claims.dto.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "claims-service",
        url = "${claims.baseUrl}",
        configuration = FeignConfig.class,
        fallback = ClaimsServiceClientFallback.class)
public interface ClaimsServiceClient {

    @GetMapping("/_/claims/stat")
    Map<String, Long> statistics(@RequestHeader("Authorization") String token);

    @GetMapping("/_/claims/listclaims/{userId}")
    List<Claim> listByUserId(@PathVariable("userId") String userId, @RequestHeader("Authorization") String token);

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
    void updateState(@RequestBody ClaimStateUpdate state, @RequestHeader("Authorization") String token);

    @PostMapping("/_/claims/updatereserve")
    void updateReserve(@RequestBody ClaimReserveUpdate reserve, @RequestHeader("Authorization") String token);

    @PostMapping("/_/claims/updatetype")
    void updateType(@RequestBody ClaimTypeUpdate type, @RequestHeader("Authorization") String token);

}
