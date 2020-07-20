package com.hedvig.backoffice.services.product_pricing;

import com.hedvig.backoffice.services.product_pricing.dto.*;
import com.hedvig.backoffice.services.product_pricing.dto.contract.*;
import com.hedvig.backoffice.web.dto.InsuranceModificationDTO;
import com.hedvig.backoffice.web.dto.ModifyInsuranceRequestDTO;
import com.hedvig.backoffice.web.dto.ProductSortColumns;
import com.hedvig.backoffice.web.dto.ProductState;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

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

  InsuranceStatusDTO createmodifiedProduct(
    String memberId, InsuranceModificationDTO changeRequest, String token);

  void modifyProduct(String memberId, ModifyInsuranceRequestDTO request, String token);

  List<MonthlySubscriptionDTO> getMonthlyPayments(YearMonth month);

  MonthlySubscriptionDTO getMonthlyPaymentsByMember(YearMonth month, String memberId);

  List<MonthlyBordereauDTO> getMonthlyBordereauByProductType(YearMonth month,
                                                             ProductType productType);

  List<MemberSearchResultDTOExtended> extendMemberSearchResult(List<Long> memberIds);

  List<SwitchableSwitcherEmailDTO> getSwitchableSwitcherEmails();

  void markSwitchableSwitcherEmailAsReminded(UUID emailId);

  Contract getContractById(UUID contractId);

  List<Contract> getContractsByMemberId(String memberId);

  void activatePendingAgreement(UUID contractId, ActivatePendingAgreementRequest request, String token);

  void terminateContract(UUID contractId, TerminateContractRequest request, String token);

  void changeTerminationDate(UUID contractId, ChangeTerminationDateRequest request, String token);

  void revertTermination(UUID contractId, String token);

  void changeFromDate(UUID agreementId, ChangeFromDateOnAgreementRequest request, String token);

  void changeToDate(UUID agreementId, ChangeToDateOnAgreementRequest request, String token);

  ContractMarketInfo getContractMarketInfoByMemberId(String memberId);

  void regenerateCertificate(UUID agreementId, String token);

  List<PartnerCampaignSearchResponse> searchPartnerCampaigns(String code, String partnerId, LocalDate activeFrom, LocalDate activeTo);

  void assignCampaignToPartnerPercentageDiscount(AssignVoucherPercentageDiscountRequest request);

  List<PartnerResponseDto> getPartnerCampaignOwners();

  ReferralInformationDto getReferralInformation(String memberId);

  EligibleForReferralDto getEligibleForReferral(String memberId);

  Boolean manualRedeemCampaign(String memberId, ManualRedeemCampaignRequest request);

  Boolean manualUnRedeemCampaign(String memberId, ManualUnRedeemCampaignRequest request);

  ManualRedeemEnableReferralsCampaignResponse manualRedeemEnableReferralsCampaign(Market market, ManualRedeemEnableReferralsCampaignRequest request);

  List<RedeemedCampaignDto> redeemedCampaigns(String memberId);
}
