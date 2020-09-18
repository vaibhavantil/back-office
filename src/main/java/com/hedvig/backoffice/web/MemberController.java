package com.hedvig.backoffice.web;

import com.hedvig.backoffice.config.feign.ExternalServiceException;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.members.dto.InsuranceCancellationDTO;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.services.product_pricing.dto.*;
import com.hedvig.backoffice.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(path = {"/api/user", "/api/member"})
public class MemberController {

  private final MemberService memberService;
  private final ProductPricingService productPricingService;
  private final PersonnelService personnelService;

  @Autowired
  public MemberController(
    MemberService memberService,
    ProductPricingService productPricingService,
    PersonnelService personnelService) {
    this.memberService = memberService;
    this.productPricingService = productPricingService;
    this.personnelService = personnelService;
  }

  @GetMapping
  public List<MemberWebDTO> list(@AuthenticationPrincipal Principal principal) {
    return
      memberService.search(null, "", personnelService.getIdToken(principal.getName()))
        .stream()
        .map(MemberWebDTO::new)
        .collect(toList());
  }

  @GetMapping("/{memberId}")
  public MemberWebDTO findOne(
    @PathVariable String memberId, @AuthenticationPrincipal Principal principal) {
    return Optional.of(
      memberService.findByMemberId(
        memberId, personnelService.getIdToken(principal.getName())))
      .map(MemberWebDTO::new)
      .orElseThrow(() -> new ExternalServiceException("member-service not available"));
  }

  @PostMapping("/{memberId}/edit")
  public ResponseEntity<?> editMember(
    @PathVariable String memberId,
    @RequestBody MemberWebDTO dto,
    @AuthenticationPrincipal Principal principal) {
    memberService.editMember(memberId, dto.convertToMemberDTO(), personnelService.getIdToken(principal.getName()));
    return ResponseEntity.noContent().build();
  }

  @GetMapping(
    path = "/mandate/{memberId}",
    produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> insuranceMandate(
    @PathVariable String memberId, @AuthenticationPrincipal Principal principal) {
    byte[] mandate =
      productPricingService.insuranceContract(
        memberId, personnelService.getIdToken(principal.getName()));
    HttpHeaders headers = new HttpHeaders();
    String filename = "insurance-mandate-" + memberId + ".pdf";
    headers.setContentDispositionFormData(filename, filename);
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
    return new ResponseEntity<>(mandate, headers, HttpStatus.OK);
  }

  @GetMapping("/insurance/{memberId}")
  public InsuranceStatusWebDTO insurance(
    @PathVariable String memberId, @AuthenticationPrincipal Principal principal) {
    InsuranceStatusDTO insurance = productPricingService.insurance(
      memberId, personnelService.getIdToken(principal.getName()));
    return new InsuranceStatusWebDTO(insurance);
  }

  @PostMapping("/insurance/{memberId}/activate")
  public ResponseEntity<?> activate(
    @PathVariable String memberId,
    @RequestBody InsuranceActivateDTO dto,
    @AuthenticationPrincipal Principal principal) {
    productPricingService.activate(memberId, dto, personnelService.getIdToken(principal.getName()));
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/insurance/{hid}/cancel")
  public ResponseEntity<?> cancel(
    @PathVariable String hid,
    @RequestBody InsuranceCancellationDTO dto,
    @AuthenticationPrincipal Principal principal) {
    InsuranceCancellationDateDTO sendDto = new InsuranceCancellationDateDTO(
      dto.getMemberId(), dto.getInsuranceId(),
      dto.getCancellationDate().atStartOfDay(ZoneId.of("Europe/Stockholm")).toInstant());
    productPricingService.cancel(hid, sendDto, personnelService.getIdToken(principal.getName()));
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/insurance/search")
  public List<InsuranceStatusWebDTO> searchInsurance(
    @RequestParam(name = "state", required = false) ProductState state,
    @RequestParam(name = "query", defaultValue = "", required = false) String query,
    @AuthenticationPrincipal Principal principal) {
    return productPricingService.search(state, query)
      .stream()
      .map(InsuranceStatusWebDTO::new)
      .collect(toList());

  }

  @GetMapping("/insurance/searchPaged")
  public InsuranceSearchResultWebDTO searchInsurancePaged(
    @RequestParam(name = "state", required = false) ProductState state,
    @RequestParam(name = "query", defaultValue = "", required = false) String query,
    @RequestParam(name = "page", required = false) Integer page,
    @RequestParam(name = "pageSize", required = false) Integer pageSize,
    @RequestParam(name = "sortBy", required = false) ProductSortColumns sortBy,
    @RequestParam(name = "sortDirection", required = false) Sort.Direction sortDirection,
    @AuthenticationPrincipal Principal principal
  ) {
    InsuranceSearchResultDTO res = productPricingService.searchPaged(state, query, page, pageSize, sortBy, sortDirection);
    return new InsuranceSearchResultWebDTO(res);
  }

  @GetMapping("/insurance/{memberId}/insurances")
  public List<InsuranceStatusWebDTO> getInsurancesByMember(
    @PathVariable String memberId, @AuthenticationPrincipal Principal principal) {
    return productPricingService.getInsurancesByMember(memberId, personnelService.getIdToken(principal.getName()))
      .stream()
      .map(InsuranceStatusWebDTO::new)
      .collect(toList());
  }

  @PostMapping("/insurance/{memberId}/sendCancellationEmail")
  public ResponseEntity<?> sendCancellationEmail(
    @PathVariable String memberId, @AuthenticationPrincipal Principal principal) {
    productPricingService.sendCancellationEmail(
      memberId, personnelService.getIdToken(principal.getName()));
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/insurance/{memberId}/certificate")
  public ResponseEntity<?> insuranceCertificate(
    @PathVariable String memberId,
    @RequestParam MultipartFile file,
    @AuthenticationPrincipal Principal principal)
    throws IOException {
    byte[] data = file.getBytes();
    productPricingService.uploadCertificate(
      memberId,
      file.getOriginalFilename(),
      file.getContentType(),
      data,
      personnelService.getIdToken(principal.getName()));
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/insurance/{memberId}/insuredAtOtherCompany")
  public ResponseEntity<?> setInsuredAtOtherCompany(
    @PathVariable String memberId, @RequestBody @Valid InsuredAtOtherCompanyDTO dto, @AuthenticationPrincipal Principal principal) {
    productPricingService.setInsuredAtOtherCompany(memberId, dto, personnelService.getIdToken(principal.getName()));
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{memberId}/setFraudulentStatus")
  public ResponseEntity<?> setFraudulentStatus(
    @PathVariable String memberId, @RequestBody @Valid MemberFraudulentStatusDTO dto, @AuthenticationPrincipal Principal principal) {
    memberService.setFraudulentStatus(memberId, dto, personnelService.getIdToken(principal.getName()));
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/insurance/{memberId}/createmodifiedProduct")
  public ResponseEntity<InsuranceStatusWebDTO> createmodifiedProduct(
    @PathVariable("memberId") String memberId,
    @RequestBody @Valid InsuranceModificationDTO changeRequest,
    @AuthenticationPrincipal Principal principal) {
    InsuranceStatusDTO insurance = productPricingService.createmodifiedProduct(memberId, changeRequest, principal.getName());
    return ResponseEntity.ok(new InsuranceStatusWebDTO(insurance));
  }

  @PostMapping("/insurance/{memberId}/modifyProduct")
  public ResponseEntity<?> modifyProduct(
    @PathVariable("memberId") String memberId,
    @RequestBody ModifyInsuranceRequestDTO request,
    @AuthenticationPrincipal Principal principal) {
    productPricingService.modifyProduct(memberId, request, principal.getName());
    return ResponseEntity.noContent().build();
  }
}
