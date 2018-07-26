package com.hedvig.backoffice.services.product_pricing;

import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuredAtOtherCompanyDTO;
import com.hedvig.backoffice.services.product_pricing.dto.MonthlySubscriptionDTO;
import com.hedvig.backoffice.web.dto.InsuranceModificationDTO;
import com.hedvig.backoffice.web.dto.InsuranceStatusDTO;
import com.hedvig.backoffice.web.dto.ModifyInsuranceRequestDTO;
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

    @Override
    public InsuranceStatusDTO createmodifiedProduct(String memberId, InsuranceModificationDTO changeRequest,
            String token) {
        log.error("product-pricing service unavailable");
        return null;
    }

    @Override
    public void modifyProduct(String memberId, ModifyInsuranceRequestDTO request, String token) {
        log.error("product-pricing service unavailable");
    }

    @Override
    public List<MonthlySubscriptionDTO> getMonthlySubscriptions(int year, int month) {
        log.error("product-pricing service getMonthlySubscriptions something went wrong");
        return null;
    }
}
