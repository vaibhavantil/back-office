package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.members.MemberNotFoundException;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.members.MemberServiceException;
import com.hedvig.backoffice.services.product_pricing.ProductPricingClient;
import com.hedvig.backoffice.web.dto.MemberDTO;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = {"/api/user", "/api/member"})
public class MemberController {

    private final MemberService memberService;

    private final ProductPricingClient productPricingClient;

    @Autowired
    public MemberController(
            MemberService memberService,
            ProductPricingClient productPricingClient
    ) {
        this.memberService = memberService;
        this.productPricingClient = productPricingClient;
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

    @GetMapping("/mandate/{hid}")
    public ResponseEntity<byte[]> insuranceMandate(@PathVariable String hid) {
        val mandate = productPricingClient.insuranceContract(hid);
        val headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        val filename = "insurance-mandate-" + hid + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(mandate, headers, HttpStatus.OK);
    }

}
