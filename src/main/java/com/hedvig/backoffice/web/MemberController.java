package com.hedvig.backoffice.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.config.feign.ExternalServiceException;
import com.hedvig.backoffice.services.members.MemberNotFoundException;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.members.MemberServiceException;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.web.dto.MemberDTO;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = {"/api/user", "/api/member"})
public class MemberController {

    private final MemberService memberService;
    private final ProductPricingService productPricingService;


    @Autowired
    public MemberController(MemberService memberService, ProductPricingService productPricingService) {
        this.memberService = memberService;
        this.productPricingService = productPricingService;
    }

    @GetMapping
    public List<MemberDTO> list() {
        return memberService.search("", "").orElseThrow(MemberServiceException::new);
    }

    @GetMapping("/{hid}")
    public MemberDTO findOne(@PathVariable String hid) throws MemberNotFoundException {
        return memberService.findByHid(hid).orElseThrow(MemberServiceException::new);
    }

    @GetMapping("/search")
    public List<MemberDTO> search(@RequestParam(name = "status", defaultValue = "", required = false) String status,
                                  @RequestParam(name = "query", defaultValue = "", required = false) String query) {
        return memberService.search(status, query).orElseThrow(MemberServiceException::new);
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

}
