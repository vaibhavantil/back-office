package com.hedvig.backoffice.services.product_pricing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.config.feign.ExternalServiceNotFoundException;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ProductPricingServiceStub implements ProductPricingService {

    private final static String INSURANCE_TEMPLATE = "{\n" +
            "  \"safetyIncreasers\": [\n" +
            "    \"Brandvarnare\",\n" +
            "    \"Säkerhetsdörr\"\n" +
            "  ],\n" +
            "  \"insuranceStatus\": \"PENDING\",\n" +
            "  \"currentTotalPrice\": 139,\n" +
            "  \"newTotalPrice\": null,\n" +
            "  \"insuredAtOtherCompany\": true,\n" +
            "  \"insuranceType\": \"BRF\",\n" +
            "  \"insuranceActiveFrom\": null,\n" +
            "  \"insuanceActiveTo\": null\n" +
            "}";

    private ConcurrentHashMap<String, String> insurances;
    private final ObjectMapper mapper;

    @Autowired
    public ProductPricingServiceStub(ObjectMapper mapper) {
        this.insurances = new ConcurrentHashMap<>();
        this.mapper = mapper;
    }

    @Override
    public byte[] insuranceContract(String memberId, String token) {
        return new byte[0];
    }

    @Override
    public JsonNode insurance(String memberId, String token) {
        if (memberId.startsWith("123")) {
            throw new ExternalServiceNotFoundException("insurance not found", "");
        }

        try {
            return mapper.readValue(insurances.computeIfAbsent(memberId, id -> INSURANCE_TEMPLATE), JsonNode.class);
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
}
