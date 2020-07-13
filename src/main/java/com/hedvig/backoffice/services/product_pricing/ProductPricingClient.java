package com.hedvig.backoffice.services.product_pricing;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.product_pricing.dto.*;
import com.hedvig.backoffice.services.product_pricing.dto.contract.*;
import com.hedvig.backoffice.web.dto.InsuranceModificationDTO;
import com.hedvig.backoffice.web.dto.ModifyInsuranceRequestDTO;
import com.hedvig.backoffice.web.dto.ProductSortColumns;
import com.hedvig.backoffice.web.dto.ProductState;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@FeignClient(
  name = "product-pricing-service",
  url = "${productPricing.baseUrl}",
  configuration = FeignConfig.class)
public interface ProductPricingClient {

  @GetMapping("/_/insurance/contract/{memberId}")
  byte[] insuranceContract(
    @PathVariable("memberId") String memberId, @RequestHeader("Authorization") String token);

  @GetMapping("/_/insurance/{memberId}/insurance")
  InsuranceStatusDTO insurance(
    @PathVariable("memberId") String memberId, @RequestHeader("Authorization") String token);

  @PostMapping("/_/insurance/{memberId}/activateAtDate")
  void activate(
    @PathVariable("memberId") String memberId,
    @RequestBody InsuranceActivateDTO dto,
    @RequestHeader("Authorization") String token);

  @PostMapping("/_/insurance/{memberId}/setCancellationDateBO")
  void cancelInsurance(
    @PathVariable("memberId") String memberId,
    @RequestBody InsuranceCancellationDateDTO dto,
    @RequestHeader("Authorization") String token);


  @GetMapping("/_/insurance/search?state={state}&query={query}")
  List<InsuranceStatusDTO> search(
    @PathVariable("state") ProductState state,
    @PathVariable("query") String query,
    @RequestHeader("Authorization") String token);

  @GetMapping("/_/insurance/searchPaged")
  InsuranceSearchResultDTO searchPaged(
    @RequestParam("query") String query,
    @RequestParam("state") ProductState state,
    @RequestParam("page") Integer page,
    @RequestParam("pageSize") Integer pageSize,
    @RequestParam("sortBy") ProductSortColumns sortBy,
    @RequestParam("sortDirection") Sort.Direction sortDirection,
    @RequestHeader("Authorization") String token);

  @PostMapping("/_/insurance/{memberId}/sendCancellationEmail")
  void sendCancellationEmail(
    @PathVariable("memberId") String memberId, @RequestHeader("Authorization") String token);

  @PostMapping("/_/insurance/{memberId}/insuredAtOtherCompany")
  void insuredAtOtherCompany(
    @PathVariable("memberId") String memberId, @RequestBody InsuredAtOtherCompanyDTO dto, @RequestHeader("Authorization") String token);

  @GetMapping("/_/insurance/{memberId}/insurances")
  List<InsuranceStatusDTO> getInsurancesByMember(
    @PathVariable("memberId") String memberId, @RequestHeader("Authorization") String token);

  @PostMapping("/_/insurance/{memberId}/createmodifiedProduct")
  InsuranceStatusDTO createmodifiedProduct(
    @PathVariable("memberId") String memberId,
    @RequestBody InsuranceModificationDTO changeRequest,
    @RequestHeader("Authorization") String token);

  @PostMapping("/_/insurance/{memberId}/modifyProduct")
  void modifyProduct(
    @PathVariable("memberId") String memberId,
    @RequestBody ModifyInsuranceRequestDTO request,
    @RequestHeader("Authorization") String token);

  @GetMapping("/_/insurance/monthlyBilling?year={year}&month={month}")
  List<MonthlySubscriptionDTO> getMonthlySubscriptions(
    @PathVariable("year") int year, @PathVariable("month") int month);

  @GetMapping("/_/insurance/{memberId}/monthlyBilling?year={year}&month={month}")
  MonthlySubscriptionDTO getMonthlySubscriptionByMember(
    @PathVariable("year") int year,
    @PathVariable("month") int month,
    @PathVariable("memberId") String memberId);

  @GetMapping("/_/insurance/bordereau?year={year}&month={month}&productType={productType}")
  List<MonthlyBordereauDTO> getMonthlyBordereauByProductType(
    @PathVariable("year") int year,
    @PathVariable("month") int month,
    @PathVariable("productType") ProductType productType);

  @PostMapping("/_/insurance/extendMemberSearch")
  List<MemberSearchResultDTOExtended> extendMemberSearchResult(@RequestBody List<Long> collect);

  @GetMapping("/_/switchableSwitchers/emails")
  List<SwitchableSwitcherEmailDTO> getSwitchableSwitcherEmails();

  @PutMapping("/_/switchableSwitchers/emails/{emailId}/remind")
  void markSwitchableSwitcherEmailAsReminded(@PathVariable("emailId") UUID emailId);

  @GetMapping("/_/contracts/members/{memberId}")
  List<Contract> getContractByMemberId(@PathVariable("memberId") String memberId);

  @GetMapping("/_/contracts/{contractId}")
  Contract getContractById(@PathVariable  UUID contractId);

  @PostMapping("/_/contracts/{contractId}/activate/pending")
  void activatePendingAgreement(
    @PathVariable UUID contractId,
    @RequestBody ActivatePendingAgreementRequest request,
    @RequestHeader("Authorization") String token
  );

  @PostMapping("/_/contracts/{contractId}/terminate")
  void terminateContract(
    @PathVariable UUID contractId,
    @RequestBody TerminateContractRequest terminateContractRequest,
    @RequestHeader("Authorization") String token
  );

  @PostMapping("/_/contracts/{contractId}/termination/change/date")
  void changeTerminationDate(
    @PathVariable UUID contractId,
    @RequestBody ChangeTerminationDateRequest request,
    @RequestHeader("Authorization") String token
  );

  @PostMapping("/_/contracts/{contractId}/termination/revert")
  void revertTermination(
    @PathVariable UUID contractId,
    @RequestHeader("Authorization") String token
  );

  @PostMapping("/_/agreements/{agreementId}/change/from")
  void changeFromDate(
    @PathVariable UUID agreementId,
    @RequestBody ChangeFromDateOnAgreementRequest request,
    @RequestHeader("Authorization") String token
  );

  @PostMapping("/_/agreements/{agreementId}/change/to")
  void changeToDate(
    @PathVariable UUID agreementId,
    @RequestBody ChangeToDateOnAgreementRequest request,
    @RequestHeader("Authorization") String token
  );

  @GetMapping("/_/contracts/members/{memberId}/contract/market/info")
  ResponseEntity<ContractMarketInfo> getContractMarketInfoForMember(
    @PathVariable String memberId
  );

  @PostMapping("/_/certificates/regenerate/{agreementId}")
  void regenerateCertificate(
    @PathVariable UUID agreementId,
    @RequestHeader("Authorization") String token
  );

  @GetMapping("/i/campaign/partner/search")
  ResponseEntity<List<PartnerCampaignSearchResponse>> searchPartnerCampaigns(
    @RequestParam
      String code,
    @RequestParam
      String partnerId,
    @RequestParam
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      LocalDate activeFrom,
    @RequestParam
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      LocalDate activeTo
  );

  @PostMapping("/i/campaign/partner/voucher-percentage-discount/assign")
  void assignCampaignToPartnerPercentageDiscount(
    @RequestBody AssignVoucherPercentageDiscountRequest request
  );

  @GetMapping("/i/campaign/partner/partnerCampaignOwners")
  ResponseEntity<List<PartnerResponseDto>> getPartnerCampaignOwners();

  @GetMapping("/i/campaign/member/{memberId}/referralInformation")
  ReferralInformationDto getReferralInformation(@PathVariable String memberId);

  @GetMapping("/i/campaign/member/{memberId}/eligibleForReferral")
  EligibleForReferralDto getEligibleForReferral(@PathVariable String memberId);
}
