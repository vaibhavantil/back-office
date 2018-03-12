package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.claims.dto.Claim;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

}
