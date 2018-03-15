package com.hedvig.backoffice.services.product_pricing;

import com.hedvig.backoffice.config.feign.FeignConfig;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "product-pricing-service",
        url = "${productPricing.baseUrl}",
        configuration = FeignConfig.class,
        fallback = ProductPricingClientFallback.class)
public interface ProductPricingClient {
    @GetMapping("/_/insurance/contract/{memberId}")
    byte[] insuranceContract(@PathVariable("memberId") String memberId);
}
