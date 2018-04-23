package com.hedvig.backoffice.services.product_pricing;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ProductPricingService {

    byte[] insuranceContract(String memberId, String token);
    JsonNode insurance(String memberId, String token);
    void activate(String memberId, InsuranceActivateDTO dto, String token);
    JsonNode search(String state, String query, String token);
    void sendCancellationEmail(String memberId, String token);
    void uploadCertificate(String memberId, MultipartFile file);

}
