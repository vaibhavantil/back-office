package com.hedvig.backoffice.services.product_pricing;

import com.hedvig.backoffice.config.feign.ExternalServiceBadRequestException;
import com.hedvig.backoffice.config.feign.ExternalServiceException;
import com.hedvig.backoffice.config.feign.ExternalServiceNotFoundException;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceCancellationDateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuredAtOtherCompanyDTO;
import com.hedvig.backoffice.services.product_pricing.dto.MonthlyBordereauDTO;
import com.hedvig.backoffice.services.product_pricing.dto.MonthlySubscriptionDTO;
import com.hedvig.backoffice.services.product_pricing.dto.ProductType;
import com.hedvig.backoffice.web.dto.InsuranceModificationDTO;
import com.hedvig.backoffice.web.dto.InsuranceSearchResultDTO;
import com.hedvig.backoffice.web.dto.InsuranceStatusDTO;
import com.hedvig.backoffice.web.dto.ModifyInsuranceRequestDTO;
import java.io.IOException;
import java.time.YearMonth;
import java.util.List;

import com.hedvig.backoffice.web.dto.ProductSortColumns;
import com.hedvig.backoffice.web.dto.ProductState;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

@Slf4j
public class ProductPricingServiceImpl implements ProductPricingService {

  private ProductPricingClient client;
  private String baseUrl;

  @Autowired
  public ProductPricingServiceImpl(
    ProductPricingClient client, @Value("${productPricing.baseUrl}") String baseUrl) {
    this.client = client;
    this.baseUrl = baseUrl;
  }

  @Override
  public byte[] insuranceContract(String memberId, String token) {
    return client.insuranceContract(memberId, token);
  }

  @Override
  public InsuranceStatusDTO insurance(String memberId, String token) {
    return client.insurance(memberId, token);
  }

  @Override
  public void activate(String memberId, InsuranceActivateDTO dto, String token) {
    client.activate(memberId, dto, token);
  }

  @Override
  public void cancel(String memberId, InsuranceCancellationDateDTO dto, String token) {
    client.cancelInsurance(memberId, dto, token);
  }

  public List<InsuranceStatusDTO> search(ProductState state, String query, String token) {
    return client.search(state, query, token);
  }

  @Override
  public InsuranceSearchResultDTO searchPaged(ProductState state, String query, Integer page, Integer pageSize, ProductSortColumns sortBy, Sort.Direction sortDirection, String token) {
    return client.searchPaged(query, state, page, pageSize, sortBy, sortDirection, token);
  }

  @Override
  public void sendCancellationEmail(String memberId, String token) {
    client.sendCancellationEmail(memberId, token);
  }

  @Override
  public void uploadCertificate(
    String memberId, String fileName, String contentType, byte[] data, String token)
    throws IOException {
    OkHttpClient client = new OkHttpClient();
    RequestBody body =
      new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart(
          "file", fileName, RequestBody.create(MediaType.parse(contentType), data))
        .build();

    Request request =
      new Request.Builder()
        .url(baseUrl + "/_/insurance/" + memberId + "/certificate")
        .addHeader("Authorization", token)
        .post(body)
        .build();

    Response response = client.newCall(request).execute();
    if (response.isSuccessful()) {
      return;
    }

    HttpStatus status = HttpStatus.valueOf(response.code());

    if (!status.is2xxSuccessful()) {
      log.error(
        "insurance certificate not uploaded to product-pricing, code = "
          + response.code()
          + ", member id = "
          + memberId);
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

  @Override
  public List<InsuranceStatusDTO> getInsurancesByMember(String memberId, String token) {
    return client.getInsurancesByMember(memberId, token);
  }

  @Override
  public InsuranceStatusDTO createmodifiedProduct(
    String memberId, InsuranceModificationDTO changeRequest, String token) {
    return client.createmodifiedProduct(memberId, changeRequest, token);
  }

  @Override
  public void modifyProduct(String memberId, ModifyInsuranceRequestDTO request, String token) {
    client.modifyProduct(memberId, request, token);
  }

  @Override
  public List<MonthlySubscriptionDTO> getMonthlyPayments(YearMonth month) {
    return client.getMonthlySubscriptions(month.getYear(), month.getMonthValue());
  }

  @Override
  public MonthlySubscriptionDTO getMonthlyPaymentsByMember(YearMonth month, String memberId) {
    return client.getMonthlySubscriptionByMember(month.getYear(), month.getMonthValue(), memberId);
  }

  @Override
  public List<MonthlyBordereauDTO> getMonthlyBordereauByProductType(YearMonth month,
    ProductType productType) {
    return client
      .getMonthlyBordereauByProductType(month.getYear(), month.getMonthValue(), productType);
  }
}
