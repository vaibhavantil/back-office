package com.hedvig.backoffice.services.product_pricing;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuredAtOtherCompanyDTO;
import com.hedvig.backoffice.services.product_pricing.dto.MonthlySubscriptionDTO;
import com.hedvig.backoffice.web.dto.InsuranceModificationDTO;
import com.hedvig.backoffice.web.dto.InsuranceStatusDTO;
import com.hedvig.backoffice.web.dto.ModifyInsuranceRequestDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
        name = "product-pricing-service",
        url = "${productPricing.baseUrl}",
        configuration = FeignConfig.class,
        fallback = ProductPricingClientFallback.class)
public interface ProductPricingClient {
    @GetMapping("/_/insurance/contract/{memberId}")
    byte[] insuranceContract(@PathVariable("memberId") String memberId, @RequestHeader("Authorization") String token);

    @GetMapping("/_/insurance/{memberId}/insurance")
    InsuranceStatusDTO insurance(@PathVariable("memberId") String memberId,
                                 @RequestHeader("Authorization") String token);

    @PostMapping("/_/insurance/{memberId}/activateAtDate")
    void activate(@PathVariable("memberId") String memberId,
                  @RequestBody InsuranceActivateDTO dto,
                  @RequestHeader("Authorization") String token);

    @GetMapping("/_/insurance/search?state={state}&query={query}")
    List<InsuranceStatusDTO> search(@PathVariable("state") String state,
                                    @PathVariable("query") String query,
                                    @RequestHeader("Authorization") String token);

    @PostMapping("/_/insurance/{memberId}/sendCancellationEmail")
    void sendCancellationEmail(@PathVariable("memberId") String memberId, @RequestHeader("Authorization") String token);

    @PostMapping("/_/insurance/{memberId}/insuredAtOtherCompany")
    void insuredAtOtherCompany(@PathVariable("memberId") String memberId, @RequestBody InsuredAtOtherCompanyDTO dto);

    @GetMapping("/_/insurance/{memberId}/insurances")
    List<InsuranceStatusDTO> getInsurancesByMember(@PathVariable("memberId") String memberId, @RequestHeader("Authorization") String token);

    @PostMapping("/_/insurance/{memberId}/createmodifiedProduct")
    InsuranceStatusDTO createmodifiedProduct(@PathVariable("memberId") String memberId, @RequestBody InsuranceModificationDTO changeRequest, @RequestHeader("Authorization") String token);

    @PostMapping("/_/insurance/{memberId}/modifyProduct")
    void modifyProduct( @PathVariable("memberId") String memberId, @RequestBody ModifyInsuranceRequestDTO request, @RequestHeader("Authorization") String token);

    @GetMapping("/_/insurance/monthlyBilling?year={year}&month={month}")
    List<MonthlySubscriptionDTO> getMonthlySubscriptions(@PathVariable("year") int year, @PathVariable("month") int month);
}
