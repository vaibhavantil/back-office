package com.hedvig.backoffice.services.product_pricing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.config.feign.ExternalServiceNotFoundException;
import com.hedvig.backoffice.services.members.MemberServiceStub;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuredAtOtherCompanyDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@Slf4j
public class ProductPricingServiceStub implements ProductPricingService {

    private final static String INSURANCE_TEMPLATE = "{\n" +
            "    \"productId\": \"%s\",\n" +
            "    \"memberId\": \"%s\",\n" +
            "    \"memberFirstName\": \"test name\",\n" +
            "    \"memberLastName\": \"test family\",\n" +
            "    \"safetyIncreasers\": [\n" +
            "      \"Brandvarnare\",\n" +
            "      \"Säkerhetsdörr\"\n" +
            "    ],\n" +
            "    \"insuranceStatus\": \"PENDING\",\n" +
            "    \"insuranceState\": \"%s\",\n" +
            "    \"currentTotalPrice\": 139,\n" +
            "    \"personsInHouseHold\": 5,\n" +
            "    \"newTotalPrice\": null,\n" +
            "    \"insuredAtOtherCompany\": true,\n" +
            "    \"insuranceType\": \"BRF\",\n" +
            "    \"cancellationEmailSent\": false,\n" +
            "    \"certificateUploaded\": false,\n" +
            "    \"insuranceActiveFrom\": null,\n" +
            "    \"insuranceActiveTo\": null\n" +
            "  }";

    private ConcurrentHashMap<String, String> insurances;
    private final ObjectMapper mapper;

    @Autowired
    public ProductPricingServiceStub(ObjectMapper mapper) {
        this.insurances = new ConcurrentHashMap<>();
        String[] states = { "QUOTE", "SIGNED", "TERMINATED" };

        IntStream.range(0, MemberServiceStub.testMemberIds.length).forEach(i ->
                insurances.put(Long.toString(MemberServiceStub.testMemberIds[i]),
                String.format(INSURANCE_TEMPLATE,
                    UUID.randomUUID().toString(),
                    MemberServiceStub.testMemberIds[i],
                    states[RandomUtils.nextInt(0, states.length)])));

        this.mapper = mapper;
    }

    @Override
    public byte[] insuranceContract(String memberId, String token) {
        return new byte[0];
    }

    @Override
    public JsonNode insurance(String memberId, String token) {
        try {
            String data = insurances.get(memberId);
            if (data == null) {
                throw new ExternalServiceNotFoundException("insurance not found", "");
            }
            return mapper.readValue(data, JsonNode.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void activate(String memberId, InsuranceActivateDTO dto, String token) {
        String i = insurances.computeIfAbsent(memberId, id -> INSURANCE_TEMPLATE);
        i = i.replace("\"insuranceActiveFrom\": null", "\"insuranceActiveFrom\": \"" + dto.getActivationDate().toString() + "\"");
        insurances.put(memberId, i);
    }

    @Override
    public JsonNode search(String state, String query, String token) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        insurances.values().forEach(v -> {
            builder.append(v);
            builder.append(",");
        });
        builder.setCharAt(builder.length() - 1, ']');

        try {
            return mapper.readValue(builder.toString(), JsonNode.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void sendCancellationEmail(String memberId, String token) {
        String i = insurances.computeIfAbsent(memberId, id -> INSURANCE_TEMPLATE);
        i = i.replace("\"cancellationEmailSent\": false", "\"cancellationEmailSent\": true");
        insurances.put(memberId, i);
    }

    @Override
    public void uploadCertificate(String memberId, String fileName, String contentType, byte[] data, String token) {
        log.info("certificate uploaded: hid = " + memberId + ", name = " + fileName);
        String i = insurances.computeIfAbsent(memberId, id -> INSURANCE_TEMPLATE);
        i = i.replace("\"certificateUploaded\": false", "\"certificateUploaded\": true");
        insurances.put(memberId, i);
    }

    @Override
    public void setInsuredAtOtherCompany(String memberId, InsuredAtOtherCompanyDTO dto) {
        String i = insurances.computeIfAbsent(memberId, id -> INSURANCE_TEMPLATE);
        i = i.replace("\"insuredAtOtherCompany\": " + (dto.isInsuredAtOtherCompany() ? "false" : "true"),
                "\"insuredAtOtherCompany\": " + (dto.isInsuredAtOtherCompany() ? "true" : "false"));
        insurances.put(memberId, i);
    }
}
