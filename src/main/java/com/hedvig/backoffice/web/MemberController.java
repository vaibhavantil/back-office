package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.members.MemberNotFoundException;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.members.MemberServiceException;
import com.hedvig.backoffice.web.dto.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = { "/api/user", "/api/member" })
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
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
    public List<MemberDTO> find(@RequestParam(name = "status", defaultValue = "", required = false) String status,
                                @RequestParam(name = "query", defaultValue = "", required = false) String query) {
        return memberService.search(status, query).orElseThrow(MemberServiceException::new);
    }

}
