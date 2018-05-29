package com.hedvig.backoffice.services.product_pricing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.config.feign.ExternalServiceNotFoundException;
import com.hedvig.backoffice.services.members.MemberServiceStub;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuredAtOtherCompanyDTO;
import com.hedvig.backoffice.web.dto.InsuranceStatusDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class ProductPricingServiceStub implements ProductPricingService {

    private List<InsuranceStatusDTO> insurances;

    @Autowired
    public ProductPricingServiceStub(ObjectMapper mapper) {
        String[] states = { "QUOTE", "SIGNED", "TERMINATED" };
        List<String> safetyIncreasers= Arrays.asList("Brandvarnare", "Säkerhetsdörr");

        insurances = IntStream.range(0, MemberServiceStub.testMemberIds.length).mapToObj(i -> {
            String memberId = Long.toString(MemberServiceStub.testMemberIds[i]);

            InsuranceStatusDTO insurance = new InsuranceStatusDTO(UUID.randomUUID().toString(),
                    memberId, ("Firstname" + memberId), ("Lastname" + memberId), safetyIncreasers,
                    "PENDING", states[RandomUtils.nextInt(0, states.length)], RandomUtils.nextInt(0,9),
                    new BigDecimal(Math.random()), null, true, "BRF",
                    null, null, false, false);

            return insurance;
        }).collect(Collectors.toList());
    }

    @Override
    public byte[] insuranceContract(String memberId, String token) {
        return new byte[0];
    }

    @Override
    public InsuranceStatusDTO insurance(String memberId, String token) {
            Optional<InsuranceStatusDTO> insurance = insurances.stream().filter(i -> i.getMemberId().equals(memberId)).findAny();
            if (!insurance.isPresent()) {
                throw new ExternalServiceNotFoundException("insurance not found", "");
            }
            return insurance.get();
    }

    @Override
    public void activate(String memberId, InsuranceActivateDTO dto, String token) {
        this.insurance(memberId,token)
                .setInsuranceActiveFrom(dto.getActivationDate().atTime(RandomUtils.nextInt(0,9), RandomUtils.nextInt(0,9)));
    }

    @Override
    public List<InsuranceStatusDTO> search(String state, String query, String token) {
        if (StringUtils.isBlank(state) && StringUtils.isBlank(query)) {
            return insurances;
        }
        return insurances.stream().filter(u -> (StringUtils.isNotBlank(query) && u.getMemberFirstName().contains(query))
                || (StringUtils.isNotBlank(state) && u.getInsuranceState().contains(state)))
                .collect(Collectors.toList());
    }

    @Override
    public void sendCancellationEmail(String memberId, String token) {
        this.insurance(memberId,token).setCancellationEmailSent(true);
    }

    @Override
    public void uploadCertificate(String memberId, String fileName, String contentType, byte[] data, String token) {
        log.info("certificate uploaded: memberId = " + memberId + ", name = " + fileName);
        this.insurance(memberId,token).setCertificateUploaded(true);
    }

    @Override
    public void setInsuredAtOtherCompany(String memberId, InsuredAtOtherCompanyDTO dto) {
        this.insurance(memberId, "").setInsuredAtOtherCompany(dto.isInsuredAtOtherCompany());
    }
}
