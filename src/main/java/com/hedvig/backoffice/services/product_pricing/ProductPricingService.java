package com.hedvig.backoffice.services.product_pricing;

import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuredAtOtherCompanyDTO;
import com.hedvig.backoffice.services.product_pricing.dto.MonthlySubscriptionDTO;
import com.hedvig.backoffice.web.dto.InsuranceModificationDTO;
import com.hedvig.backoffice.web.dto.InsuranceStatusDTO;
import com.hedvig.backoffice.web.dto.ModifyInsuranceRequestDTO;
import java.io.IOException;
import java.time.YearMonth;
import java.util.List;

public interface ProductPricingService {

  byte[] insuranceContract(String memberId, String token);

  InsuranceStatusDTO insurance(String memberId, String token);

  void activate(String memberId, InsuranceActivateDTO dto, String token);

  List<InsuranceStatusDTO> search(String state, String query, String token);

  void sendCancellationEmail(String memberId, String token);

  void uploadCertificate(
      String memberId, String fileName, String contentType, byte[] data, String token)
      throws IOException;

  void setInsuredAtOtherCompany(String memberId, InsuredAtOtherCompanyDTO dto);

  List<InsuranceStatusDTO> getInsurancesByMember(String memberId, String token);

  InsuranceStatusDTO createmodifiedProduct(
      String memberId, InsuranceModificationDTO changeRequest, String token);

  void modifyProduct(String memberId, ModifyInsuranceRequestDTO request, String token);

  List<MonthlySubscriptionDTO> getMonthlyPayments(YearMonth month);

  MonthlySubscriptionDTO getMonthlyPaymentsByMember(YearMonth month, String memberId);
}
