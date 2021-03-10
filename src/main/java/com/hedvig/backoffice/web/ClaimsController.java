package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.dto.Claim;
import com.hedvig.backoffice.services.claims.dto.ClaimSearchResultDTO;
import com.hedvig.backoffice.services.claims.dto.ClaimSortColumn;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import java.security.Principal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/claims")
public class ClaimsController {
    private static final Logger log = LoggerFactory.getLogger(ClaimsController.class);

    private final ClaimsService claimsService;
    private final PersonnelService personnelService;

    @Autowired
    public ClaimsController(ClaimsService claimsService, PersonnelService personnelService) {
        this.claimsService = claimsService;
        this.personnelService = personnelService;
    }

    @GetMapping("/search")
    public ClaimSearchResultDTO search(@RequestParam(name = "page", required = false) Integer page,
                                       @RequestParam(name = "pageSize", required = false) Integer pageSize,
                                       @RequestParam(name = "sortBy", required = false) ClaimSortColumn sortBy,
                                       @RequestParam(name = "sortDirection", required = false) Sort.Direction sortDirection,
                                       @AuthenticationPrincipal Principal principal) {
        String idToken = personnelService.getIdToken(principal.getName());
        return claimsService.search(page, pageSize, sortBy, sortDirection, idToken);
    }

    @GetMapping("/user/{id}")
    public List<Claim> listByUserId(@PathVariable String id,
                                    @AuthenticationPrincipal Principal principal) {
        return claimsService.listByUserId(id, personnelService.getIdToken(principal.getName()));
    }
}
