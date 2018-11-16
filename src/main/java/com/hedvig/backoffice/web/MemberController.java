package com.hedvig.backoffice.web;

import com.hedvig.backoffice.config.feign.ExternalServiceException;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.members.dto.InsuranceCancellationDTO;
import com.hedvig.backoffice.services.members.dto.MembersSortColumn;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceCancellationDateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuredAtOtherCompanyDTO;

import java.io.IOException;
import java.security.Principal;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import com.hedvig.backoffice.web.dto.InsuranceModificationDTO;
import com.hedvig.backoffice.web.dto.InsuranceSearchResultDTO;
import com.hedvig.backoffice.web.dto.InsuranceStatusDTO;
import com.hedvig.backoffice.web.dto.MemberDTO;
import com.hedvig.backoffice.web.dto.MemberFraudulentStatusDTO;
import com.hedvig.backoffice.web.dto.MemberStatus;
import com.hedvig.backoffice.web.dto.MembersSearchResultDTO;
import com.hedvig.backoffice.web.dto.ModifyInsuranceRequestDTO;
import com.hedvig.backoffice.web.dto.ProductSortColumns;
import com.hedvig.backoffice.web.dto.ProductState;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
  public List<MemberDTO> list(@AuthenticationPrincipal Principal principal) {
    return memberService.search(null, "", personnelService.getIdToken(principal.getName()));
  }

  @GetMapping("/{memberId}")
  public MemberDTO findOne(
      @PathVariable String memberId, @AuthenticationPrincipal Principal principal) {
    return Optional.ofNullable(
            memberService.findByMemberId(
                memberId, personnelService.getIdToken(principal.getName())))
        .orElseThrow(() -> new ExternalServiceException("member-service not available"));
  }

  @PostMapping("/{memberId}/edit")
  public ResponseEntity<?> editMember(
      @PathVariable String memberId,
      @RequestBody MemberDTO dto,
      @AuthenticationPrincipal Principal principal) {
    memberService.editMember(memberId, dto, personnelService.getIdToken(principal.getName()));
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/search")
  public List<MemberDTO> search(
      @RequestParam(name = "status", required = false) MemberStatus status,
      @RequestParam(name = "query", defaultValue = "", required = false) String query,
      @AuthenticationPrincipal Principal principal) {
    return memberService.search(status, query, personnelService.getIdToken(principal.getName()));
  }

  @GetMapping("/searchPaged")
  public MembersSearchResultDTO searchPaged(
    @RequestParam(name = "status", required = false) MemberStatus status,
    @RequestParam(name = "query", required = false) String query,
    @RequestParam(name = "page", required = false) Integer page,
    @RequestParam(name = "pageSize", required = false) Integer pageSize,
    @RequestParam(name = "sortBy", required = false) MembersSortColumn sortBy,
    @RequestParam(name = "sortDirection", required = false) Sort.Direction sortDirection,

    @AuthenticationPrincipal Principal principal) {
    String token = personnelService.getIdToken(principal.getName());
    return memberService.searchPaged(status, query, page, pageSize, sortBy, sortDirection, token);
  }

  @RequestMapping(
      path = "/mandate/{memberId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> insuranceMandate(
      @PathVariable String memberId, @AuthenticationPrincipal Principal principal) {
    val mandate =
        productPricingService.insuranceContract(
            memberId, personnelService.getIdToken(principal.getName()));
    val headers = new HttpHeaders();
    val filename = "insurance-mandate-" + memberId + ".pdf";
    headers.setContentDispositionFormData(filename, filename);
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
    return new ResponseEntity<>(mandate, headers, HttpStatus.OK);
  }

  @GetMapping("/insurance/{memberId}")
  public InsuranceStatusDTO insurance(
      @PathVariable String memberId, @AuthenticationPrincipal Principal principal) {
    return productPricingService.insurance(
        memberId, personnelService.getIdToken(principal.getName()));
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
//    memberService.cancelInsurance(hid, dto, personnelService.getIdToken(principal.getName()));
    productPricingService.cancel(hid, sendDto, personnelService.getIdToken(principal.getName()));
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/insurance/search")
  public List<InsuranceStatusDTO> searchInsurance(
      @RequestParam(name = "state", required = false) ProductState state,
      @RequestParam(name = "query", defaultValue = "", required = false) String query,
      @AuthenticationPrincipal Principal principal) {
    return productPricingService.search(
        state, query, personnelService.getIdToken(principal.getName()));
  }

  @GetMapping("/insurance/searchPaged")
  public InsuranceSearchResultDTO searchInsurancePaged(
    @RequestParam(name = "state", required = false) ProductState state,
    @RequestParam(name = "query", defaultValue = "", required = false) String query,
    @RequestParam(name = "page", required = false) Integer page,
    @RequestParam(name = "pageSize", required = false) Integer pageSize,
    @RequestParam(name = "sortBy", required = false) ProductSortColumns sortBy,
    @RequestParam(name = "sortDirection", required = false) Sort.Direction sortDirection,
    @AuthenticationPrincipal Principal principal) {
    String idToken = personnelService.getIdToken(principal.getName());
    return productPricingService.searchPaged(state, query, page, pageSize, sortBy, sortDirection, idToken);
  }

  @GetMapping("/insurance/{memberId}/insurances")
  public List<InsuranceStatusDTO> getInsurancesByMember(
      @PathVariable String memberId, @AuthenticationPrincipal Principal principal) {
    return productPricingService.getInsurancesByMember(
        memberId, personnelService.getIdToken(principal.getName()));
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

  @PostMapping("/insurance/{memberId}/fraudulentStatus")
  public ResponseEntity<?> setFraudulentStatus(
    @PathVariable String memberId, @RequestBody @Valid MemberFraudulentStatusDTO dto, @AuthenticationPrincipal Principal principal) {
    memberService.setFraudulentStatus(memberId, dto, personnelService.getIdToken(principal.getName()));
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/insurance/{memberId}/createmodifiedProduct")
  public ResponseEntity<InsuranceStatusDTO> createmodifiedProduct(
      @PathVariable("memberId") String memberId,
      @RequestBody @Valid InsuranceModificationDTO changeRequest,
      @AuthenticationPrincipal Principal principal) {
    return ResponseEntity.ok(
        productPricingService.createmodifiedProduct(memberId, changeRequest, principal.getName()));
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
