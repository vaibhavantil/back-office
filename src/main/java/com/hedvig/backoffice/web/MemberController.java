package com.hedvig.backoffice.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.config.feign.ExternalServiceException;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import com.hedvig.backoffice.web.dto.MemberDTO;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<byte[]> insuranceMandate(@PathVariable String hid) {
        val mandate = productPricingService.insuranceContract(hid);
        val headers = new HttpHeaders();
        val filename = "insurance-mandate-" + hid + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(mandate, headers, HttpStatus.OK);
    }

    @GetMapping("/insurance/{hid}")
    public JsonNode insurance(@PathVariable String hid) {
        return Optional.ofNullable(productPricingService.insurance(hid))
                .orElseThrow(() -> new ExternalServiceException("request to product-pricing service failed"));
    }

    @PostMapping("/insurance/{hid}/activate")
    public ResponseEntity<?> activate(@PathVariable String hid, @RequestBody InsuranceActivateDTO dto) {
        productPricingService.activate(hid, dto);
        return ResponseEntity.noContent().build();
    }

}
