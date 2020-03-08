package com.hedvig.backoffice.services.product_pricing;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.product_pricing.dto.*;
import com.hedvig.backoffice.services.product_pricing.dto.contract.ActivatePendingAgreementRequest;
import com.hedvig.backoffice.services.product_pricing.dto.contract.ChangeTerminationDateRequest;
import com.hedvig.backoffice.services.product_pricing.dto.contract.Contract;
import com.hedvig.backoffice.services.product_pricing.dto.contract.TerminateContractRequest;
import com.hedvig.backoffice.web.dto.InsuranceModificationDTO;
import com.hedvig.backoffice.web.dto.ModifyInsuranceRequestDTO;
import com.hedvig.backoffice.web.dto.ProductSortColumns;
import com.hedvig.backoffice.web.dto.ProductState;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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

  @GetMapping("/_/contracts/member/{memberId}")
  List<Contract> getContractsOfMember(@PathVariable("memberId") String memberId);

  @PostMapping("/_/contracts/terminate")
  void terminateContract(
    @RequestBody TerminateContractRequest terminateContractRequest,
    @RequestHeader("Authorization") String token
  );

  @GetMapping("/_/contracts/{contractId}")
  Contract getContractById(@PathVariable  UUID contractId);

  @PostMapping("/_/agreements/activate/pending")
  void activatePendingAgreement(
    @RequestBody ActivatePendingAgreementRequest request,
    @RequestHeader("Authorization") String token
  );

  @PostMapping("/_/contracts/change/termination/date")
  void changeTerminationDate(
    @RequestBody ChangeTerminationDateRequest request,
    @RequestHeader String token
  );

  @PostMapping("/_/contracts/revert/termination/{contractId}")
  void revertTermination(
    @PathVariable UUID contractId,
    @RequestHeader String token
  );
}
