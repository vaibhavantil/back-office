package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.dto.ClaimSearchResultDTO;
import com.hedvig.backoffice.services.claims.dto.ClaimSortColumn;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import java.io.IOException;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/claims")
public class ClaimsController {
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

    @PostMapping("/{claimId}/claimFiles")
    public void uploadFiles(@PathVariable("claimId") String claimId,
                            @RequestParam("files") MultipartFile[] files,
                            @RequestParam("memberId") String memberId
    ) throws IOException {
        claimsService.uploadClaimsFiles(claimId, files, memberId);
    }
}
