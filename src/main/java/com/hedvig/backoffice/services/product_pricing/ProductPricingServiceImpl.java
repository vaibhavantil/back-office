package com.hedvig.backoffice.services.product_pricing;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class ProductPricingServiceImpl implements ProductPricingService {

    private ProductPricingClient client;

    @Autowired
    public ProductPricingServiceImpl(ProductPricingClient client) {
        this.client = client;
    }

    @Override
    public byte[] insuranceContract(String memberId, String token) {
        return client.insuranceContract(memberId, token);
    }

    @Override
    public JsonNode insurance(String memberId, String token) {
        return client.insurance(memberId, token);
    }

    @Override
    public void activate(String memberId, InsuranceActivateDTO dto, String token) {
        client.activate(memberId, dto, token);
    }

    @Override
    public JsonNode search(String state, String query, String token) {
        return client.search(state, query, token);
    }

    @Override
    public void sendCancellationEmail(String memberId, String token) {
        client.sendCancellationEmail(memberId, token);
    }

    @Override
    public void uploadCertificate(String memberId, MultipartFile file) {
        // TODO implement
    }
}
