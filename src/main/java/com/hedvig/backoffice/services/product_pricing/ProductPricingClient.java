package com.hedvig.backoffice.services.product_pricing;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuredAtOtherCompanyDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "product-pricing-service",
        url = "${productPricing.baseUrl}",
        configuration = FeignConfig.class,
        fallback = ProductPricingClientFallback.class)
public interface ProductPricingClient {
    @GetMapping("/_/insurance/contract/{memberId}")
    byte[] insuranceContract(@PathVariable("memberId") String memberId, @RequestHeader("Authorization") String token);

    @GetMapping("/_/insurance/{memberId}/insurance")
    JsonNode insurance(@PathVariable("memberId") String memberId, @RequestHeader("Authorization") String token);

    @PostMapping("/_/insurance/{memberId}/activateAtDate")
    void activate(@PathVariable("memberId") String memberId, @RequestBody InsuranceActivateDTO dto, @RequestHeader("Authorization") String token);

    @GetMapping("/_/insurance/search?state={state}&query={query}")
    JsonNode search(@PathVariable("state") String state,
                    @PathVariable("query") String query,
                    @RequestHeader("Authorization") String token);

    @PostMapping("/_/insurance/{memberId}/sendCancellationEmail")
    void sendCancellationEmail(@PathVariable("memberId") String memberId, @RequestHeader("Authorization") String token);

    @PostMapping("/_/insurance/{memberId}/insuredAtOtherCompany")
    void insuredAtOtherCompany(@PathVariable("memberId") String memberId, @RequestBody InsuredAtOtherCompanyDTO dto);
}
