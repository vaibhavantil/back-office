package com.hedvig.backoffice.services.product_pricing;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuredAtOtherCompanyDTO;
import com.hedvig.backoffice.web.dto.InsuranceStatusDTO;

import java.io.IOException;
import java.util.List;

public interface ProductPricingService {

    byte[] insuranceContract(String memberId, String token);
    InsuranceStatusDTO insurance(String memberId, String token);
    void activate(String memberId, InsuranceActivateDTO dto, String token);
    List<InsuranceStatusDTO> search(String state, String query, String token);
    void sendCancellationEmail(String memberId, String token);
    void uploadCertificate(String memberId, String fileName, String contentType, byte[] data, String token) throws IOException;
    void setInsuredAtOtherCompany(String memberId, InsuredAtOtherCompanyDTO dto);

}
