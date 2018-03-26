package com.hedvig.backoffice.services.product_pricing;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;

public interface ProductPricingService {

    byte[] insuranceContract(String memberId);
    JsonNode insurance(String memberId);
    void activate(String memberId, InsuranceActivateDTO dto);

}
