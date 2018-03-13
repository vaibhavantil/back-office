package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.claims.dto.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "claims-service",
        url = "${claims.baseUrl}",
        configuration = FeignConfig.class,
        fallback = ClaimsServiceClientFallback.class)
public interface ClaimsServiceClient {

    @GetMapping("/_/claims/stat")
    Map<String, Long> statistics();

    @GetMapping("/_/claims/listclaims/{userId}")
    List<Claim> listByUserId(@PathVariable("userId") String userId);

    @GetMapping("/_/claims/listclaims")
    List<Claim> list();

    @GetMapping("/_/claims/claim?claimID={id}")
    Claim find(@PathVariable("id") String id);

    @GetMapping("/_/claims/claimTypes")
    List<ClaimType> types();

    @PostMapping("/_/claims/addpayment")
    void addPayment(@RequestBody ClaimPayment dto);

    @PostMapping("/_/claims/addnote")
    void addNote(@RequestBody ClaimNote dto);

    @PostMapping("/_/claims/adddataitem")
    void addDataItem(@RequestBody ClaimData data);

    @PostMapping("/_/claims/updatestate")
    void updateState(@RequestBody ClaimStateUpdate state);

    @PostMapping("/_/claims/updatereserve")
    void updateReserve(@RequestBody ClaimReserveUpdate reserve);

    @PostMapping("/_/claims/updatetype")
    void updateType(@RequestBody ClaimTypeUpdate type);

}
