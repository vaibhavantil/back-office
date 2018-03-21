package com.hedvig.backoffice.services.product_pricing;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductPricingClientFallback implements ProductPricingClient {

    @Override
    public byte[] insuranceContract(String memberId) {
        log.error("product-pricing service unavailable");
        return new byte[0];
    }

    @Override
    public JsonNode insurance(String memberId) {
        log.error("product-pricing service unavailable");
        return null;
    }
}