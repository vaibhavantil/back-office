package com.hedvig.backoffice.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.config.feign.ExternalServiceException;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.members.dto.InsuranceCancellationDTO;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuredAtOtherCompanyDTO;
import com.hedvig.backoffice.web.dto.InsuranceStatusDTO;
import com.hedvig.backoffice.web.dto.MemberDTO;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = {"/api/user", "/api/member"})
public class MemberController {

    private final MemberService memberService;
    private final ProductPricingService productPricingService;
    private final PersonnelService personnelService;


    @Autowired
    public MemberController(MemberService memberService,
                            ProductPricingService productPricingService,
                            PersonnelService personnelService) {
        this.memberService = memberService;
        this.productPricingService = productPricingService;
        this.personnelService = personnelService;
    }

    @GetMapping
    public List<MemberDTO> list(@AuthenticationPrincipal Principal principal) {
        return memberService.search("", "", personnelService.getIdToken(principal.getName()));
    }

    @GetMapping("/{memberId}")
    public MemberDTO findOne(@PathVariable String memberId, @AuthenticationPrincipal Principal principal) {
        return Optional.ofNullable(memberService.findByMemberId(memberId, personnelService.getIdToken(principal.getName())))
                .orElseThrow(() -> new ExternalServiceException("member-service not available"));
    }

    @PostMapping("/{memberId}/edit")
    public ResponseEntity<?> editMember(@PathVariable String memberId,
                                      @RequestBody MemberDTO dto,
                                      @AuthenticationPrincipal Principal principal) {
        memberService.editMember(memberId, dto, personnelService.getIdToken(principal.getName()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<MemberDTO> search(@RequestParam(name = "status", defaultValue = "", required = false) String status,
                                  @RequestParam(name = "query", defaultValue = "", required = false) String query,
                                  @AuthenticationPrincipal Principal principal) {
        return memberService.search(status, query, personnelService.getIdToken(principal.getName()));
    }

    @RequestMapping(path = "/mandate/{memberId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> insuranceMandate(@PathVariable String memberId, @AuthenticationPrincipal Principal principal) {
        val mandate = productPricingService.insuranceContract(memberId, personnelService.getIdToken(principal.getName()));
        val headers = new HttpHeaders();
        val filename = "insurance-mandate-" + memberId + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(mandate, headers, HttpStatus.OK);
    }

    @GetMapping("/insurance/{memberId}")
    public InsuranceStatusDTO insurance(@PathVariable String memberId, @AuthenticationPrincipal Principal principal) {
        return productPricingService.insurance(memberId, personnelService.getIdToken(principal.getName()));
    }

    @PostMapping("/insurance/{memberId}/activate")
    public ResponseEntity<?> activate(@PathVariable String memberId,
                                      @RequestBody InsuranceActivateDTO dto,
                                      @AuthenticationPrincipal Principal principal) {
        productPricingService.activate(memberId, dto, personnelService.getIdToken(principal.getName()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/insurance/{hid}/cancel")
    public ResponseEntity<?> cancel (@PathVariable String hid,
                                      @RequestBody InsuranceCancellationDTO dto,
                                      @AuthenticationPrincipal Principal principal) {
        memberService.cancelInsurance(hid, dto, personnelService.getIdToken(principal.getName()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/insurance/search")
    public List<InsuranceStatusDTO> serachInsurance(@RequestParam(name = "state", defaultValue = "", required = false) String state,
                                    @RequestParam(name = "query", defaultValue = "", required = false) String query,
                                    @AuthenticationPrincipal Principal principal) {
        return productPricingService.search(state, query, personnelService.getIdToken(principal.getName()));
    }

    @PostMapping("/insurance/{memberId}/sendCancellationEmail")
    public ResponseEntity<?> sendCancellationEmail(@PathVariable String memberId, @AuthenticationPrincipal Principal principal) {
        productPricingService.sendCancellationEmail(memberId, personnelService.getIdToken(principal.getName()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/insurance/{memberId}/certificate")
    public ResponseEntity<?> insuranceCertificate(@PathVariable String memberId,
                                                  @RequestParam MultipartFile file,
                                                  @AuthenticationPrincipal Principal principal) throws IOException {
        byte[] data = file.getBytes();
        productPricingService.uploadCertificate(memberId, file.getOriginalFilename(), file.getContentType(), data,
                personnelService.getIdToken(principal.getName()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/insurance/{memberId}/insuredAtOtherCompany")
    public ResponseEntity<?> setInsuredAtOtherCompany(@PathVariable String memberId, @RequestBody @Valid InsuredAtOtherCompanyDTO dto) {
        productPricingService.setInsuredAtOtherCompany(memberId, dto);
        return ResponseEntity.noContent().build();
    }
}
