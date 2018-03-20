package com.hedvig.backoffice.services.product_pricing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.config.feign.ExternalServiceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ProductPricingServiceStub implements ProductPricingService {

    private final static String INSURANCE_TEMPLATE = "{\n" +
            "  \"safetyIncreasers\": [\n" +
            "    \"Brandvarnare\",\n" +
            "    \"Säkerhetsdörr\"\n" +
            "  ],\n" +
            "  \"insuranceStatus\": \"PENDING\"\n" +
            "}";

    private ConcurrentHashMap<String, String> insurances;
    private final ObjectMapper mapper;

    @Autowired
    public ProductPricingServiceStub(ObjectMapper mapper) {
        this.insurances = new ConcurrentHashMap<>();
        this.mapper = mapper;
    }

    @Override
    public byte[] insuranceContract(String memberId) {
        return new byte[0];
    }

    @Override
    public JsonNode insurance(String memberId) {
        if (memberId.startsWith("123")) {
            throw new ExternalServiceNotFoundException("insurance not found", "");
        }

        try {
            return mapper.readValue(insurances.computeIfAbsent(memberId, id -> INSURANCE_TEMPLATE), JsonNode.class);
        } catch (IOException e) {
            return null;
        }
    }
}
