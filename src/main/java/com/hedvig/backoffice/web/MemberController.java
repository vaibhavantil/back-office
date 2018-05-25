package com.hedvig.backoffice.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.config.feign.ExternalServiceException;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.members.dto.InsuranceCancellationDTO;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuredAtOtherCompanyDTO;
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

    @GetMapping("/{hid}")
    public MemberDTO findOne(@PathVariable String hid, @AuthenticationPrincipal Principal principal) {
        return Optional.ofNullable(memberService.findByHid(hid, personnelService.getIdToken(principal.getName())))
                .orElseThrow(() -> new ExternalServiceException("member-service not available"));
    }

    @GetMapping("/search")
    public List<MemberDTO> search(@RequestParam(name = "status", defaultValue = "", required = false) String status,
                                  @RequestParam(name = "query", defaultValue = "", required = false) String query,
                                  @AuthenticationPrincipal Principal principal) {
        return memberService.search(status, query, personnelService.getIdToken(principal.getName()));
    }

    @RequestMapping(path = "/mandate/{hid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> insuranceMandate(@PathVariable String hid, @AuthenticationPrincipal Principal principal) {
        val mandate = productPricingService.insuranceContract(hid, personnelService.getIdToken(principal.getName()));
        val headers = new HttpHeaders();
        val filename = "insurance-mandate-" + hid + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(mandate, headers, HttpStatus.OK);
    }

    @GetMapping("/insurance/{hid}")
    public JsonNode insurance(@PathVariable String hid, @AuthenticationPrincipal Principal principal) {
        return Optional.ofNullable(productPricingService.insurance(hid, personnelService.getIdToken(principal.getName())))
                .orElseThrow(() -> new ExternalServiceException("request to product-pricing service failed"));
    }

    @PostMapping("/insurance/{hid}/activate")
    public ResponseEntity<?> activate(@PathVariable String hid,
                                      @RequestBody InsuranceActivateDTO dto,
                                      @AuthenticationPrincipal Principal principal) {
        productPricingService.activate(hid, dto, personnelService.getIdToken(principal.getName()));
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
    public JsonNode serachInsurance(@RequestParam(name = "state", defaultValue = "", required = false) String state,
                                    @RequestParam(name = "query", defaultValue = "", required = false) String query,
                                    @AuthenticationPrincipal Principal principal) {
        return productPricingService.search(state, query, personnelService.getIdToken(principal.getName()));
    }

    @PostMapping("/insurance/{hid}/sendCancellationEmail")
    public ResponseEntity<?> sendCancellationEmail(@PathVariable String hid, @AuthenticationPrincipal Principal principal) {
        productPricingService.sendCancellationEmail(hid, personnelService.getIdToken(principal.getName()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/insurance/{hid}/certificate")
    public ResponseEntity<?> insuranceCertificate(@PathVariable String hid,
                                                  @RequestParam MultipartFile file,
                                                  @AuthenticationPrincipal Principal principal) throws IOException {
        byte[] data = file.getBytes();
        productPricingService.uploadCertificate(hid, file.getOriginalFilename(), file.getContentType(), data,
                personnelService.getIdToken(principal.getName()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/insurance/{hid}/insuredAtOtherCompany")
    public ResponseEntity<?> setInsuredAtOtherCompany(@PathVariable String hid, @RequestBody @Valid InsuredAtOtherCompanyDTO dto) {
        productPricingService.setInsuredAtOtherCompany(hid, dto);
        return ResponseEntity.noContent().build();
    }
}
