package com.hedvig.backoffice.services.product_pricing;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.config.feign.ExternalServiceBadRequestException;
import com.hedvig.backoffice.config.feign.ExternalServiceException;
import com.hedvig.backoffice.config.feign.ExternalServiceNotFoundException;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuredAtOtherCompanyDTO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Slf4j
public class ProductPricingServiceImpl implements ProductPricingService {

    private ProductPricingClient client;
    private String baseUrl;

    @Autowired
    public ProductPricingServiceImpl(ProductPricingClient client,
                                     @Value("${productPricing.baseUrl}") String baseUrl) {
        this.client = client;
        this.baseUrl = baseUrl;
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
    public void uploadCertificate(String memberId, String fileName, String contentType, byte[] data, String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                        RequestBody.create(MediaType.parse(contentType), data))
                .build();

        Request request = new Request.Builder()
                .url(baseUrl + "/_/insurance/" + memberId + "/certificate")
                .addHeader("Authorization", token)
                .post(body).build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return;
        }

        HttpStatus status = HttpStatus.valueOf(response.code());

        if (!status.is2xxSuccessful()) {
            log.error("insurance certificate not uploaded to product-pricing, code = " + response.code() + ", member id = " + memberId);
        }

        if (status == HttpStatus.NOT_FOUND) {
            throw new ExternalServiceNotFoundException("member not found, id = " + memberId, "");
        } else if (status.is4xxClientError()) {
            throw new ExternalServiceBadRequestException("bad request, id = " + memberId, "");
        } else if (status.is5xxServerError()) {
            throw new ExternalServiceException("product pricing internal error");
        }
    }

    @Override
    public void setInsuredAtOtherCompany(String memberId, InsuredAtOtherCompanyDTO dto) {
        client.insuredAtOtherCompany(memberId, dto);
    }
}
