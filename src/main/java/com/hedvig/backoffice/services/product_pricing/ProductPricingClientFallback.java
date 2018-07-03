package com.hedvig.backoffice.services.product_pricing;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuredAtOtherCompanyDTO;
import com.hedvig.backoffice.web.dto.InsuranceStatusDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProductPricingClientFallback implements ProductPricingClient {

    @Override
    public byte[] insuranceContract(String memberId, String token) {
        log.error("product-pricing service unavailable");
        return new byte[0];
    }

    @Override
    public InsuranceStatusDTO insurance(String memberId, String token) {
        log.error("product-pricing service unavailable");
        return null;
    }

    @Override
    public void activate(String memberId, InsuranceActivateDTO dto, String token) {
        log.error("product-pricing service unavailable");
    }

    @Override
    public List<InsuranceStatusDTO> search(String state, String query, String token) {
        log.error("product-pricing service unavailable");
        return null;
    }

    @Override
    public void sendCancellationEmail(String memberId, String token) {
        log.error("product-pricing service unavailable");
    }

    @Override
    public void insuredAtOtherCompany(String memberId, InsuredAtOtherCompanyDTO dto) {
        log.error("product-pricing service unavailable");
    }

  @Override
  public List<InsuranceStatusDTO> getInsurancesByMember(String memberId, String token) {
    log.error("product-pricing service unavailable");
    return null;
  }
}
