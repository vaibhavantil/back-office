package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.claims.ClaimNotFoundException;
import com.hedvig.backoffice.services.claims.ClaimsServiceException;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.web.dto.ClaimDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimsController {

    private final ClaimsService claimsService;

    @Autowired
    public ClaimsController(ClaimsService claimsService) {
        this.claimsService = claimsService;
    }

    @GetMapping("/claims")
    public List<ClaimDTO> claims() throws ClaimsServiceException {
        return claimsService.list();
    }

    @GetMapping("/{id}")
    public ClaimDTO claim(@PathVariable String id) throws ClaimsServiceException, ClaimNotFoundException {
        return claimsService.find(id);
    }

}
