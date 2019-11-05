package com.hedvig.backoffice.services.product_pricing;

import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceCancellationDateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceSearchResultDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceStatusDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuredAtOtherCompanyDTO;
import com.hedvig.backoffice.services.product_pricing.dto.MonthlyBordereauDTO;
import com.hedvig.backoffice.services.product_pricing.dto.MonthlySubscriptionDTO;
import com.hedvig.backoffice.services.product_pricing.dto.ProductType;
import com.hedvig.backoffice.web.dto.ProductSortColumns;
import com.hedvig.backoffice.web.dto.ProductState;
import java.io.IOException;
import java.time.YearMonth;
import java.util.List;
import org.springframework.data.domain.Sort;

public interface ProductPricingService {

  byte[] insuranceContract(String memberId, String token);

  InsuranceStatusDTO insurance(String memberId, String token);

  void activate(String memberId, InsuranceActivateDTO dto, String token);

  void cancel(String memberId, InsuranceCancellationDateDTO dto, String token);

  List<InsuranceStatusDTO> search(ProductState state, String query, String token);

  InsuranceSearchResultDTO searchPaged(
    ProductState state,
    String query,
    Integer page,
    Integer pageSize,
    ProductSortColumns sortBy,
    Sort.Direction sortDirection,
    String token);

  void sendCancellationEmail(String memberId, String token);

  void uploadCertificate(
    String memberId, String fileName, String contentType, byte[] data, String token)
    throws IOException;

  void setInsuredAtOtherCompany(String memberId, InsuredAtOtherCompanyDTO dto, String token);

  List<InsuranceStatusDTO> getInsurancesByMember(String memberId, String token);

  List<MonthlySubscriptionDTO> getMonthlyPayments(YearMonth month);

  MonthlySubscriptionDTO getMonthlyPaymentsByMember(YearMonth month, String memberId);

  List<MonthlyBordereauDTO> getMonthlyBordereauByProductType(YearMonth month,
    ProductType productType);
}
