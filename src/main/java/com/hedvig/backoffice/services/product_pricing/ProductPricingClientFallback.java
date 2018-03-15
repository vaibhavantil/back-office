package com.hedvig.backoffice.services.product_pricing;

import org.springframework.stereotype.Component;

@Component
public class ProductPricingClientFallback implements ProductPricingClient {
    @Override
    public byte[] insuranceContract(String memberId) {
        return new byte[0];
    }
}
