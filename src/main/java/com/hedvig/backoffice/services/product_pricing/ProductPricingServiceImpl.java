package com.hedvig.backoffice.services.product_pricing;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductPricingServiceImpl implements ProductPricingService {

    private ProductPricingClient client;

    @Autowired
    public ProductPricingServiceImpl(ProductPricingClient client) {
        this.client = client;
    }

    @Override
    public byte[] insuranceContract(String memberId) {
        return client.insuranceContract(memberId);
    }

    @Override
    public JsonNode insurance(String memberId) {
        return client.insurance(memberId);
    }
}
