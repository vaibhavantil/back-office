package com.hedvig.backoffice.services.product_pricing;

import com.fasterxml.jackson.databind.JsonNode;

public interface ProductPricingService {

    byte[] insuranceContract(String memberId);
    JsonNode insurance(String memberId);

}
