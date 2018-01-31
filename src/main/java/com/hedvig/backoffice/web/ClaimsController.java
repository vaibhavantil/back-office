package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.claims.ClaimNotFoundException;
import com.hedvig.backoffice.services.claims.ClaimStatus;
import com.hedvig.backoffice.services.claims.ClaimsServiceException;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.web.dto.ClaimDTO;
import com.hedvig.backoffice.web.dto.ClaimTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimsController {

    private final ClaimsService claimsService;

    @Autowired
    public ClaimsController(ClaimsService claimsService) {
        this.claimsService = claimsService;
    }

    @GetMapping
    public List<ClaimDTO> claims() throws ClaimsServiceException {
        return claimsService.list();
    }

    @GetMapping("/{id}")
    public ClaimDTO claim(@PathVariable String id) throws ClaimsServiceException, ClaimNotFoundException {
        return claimsService.find(id);
    }

    @PostMapping("/{id}/status/{status}")
    public ResponseEntity<?> status(@PathVariable String id, @PathVariable ClaimStatus status)
            throws ClaimsServiceException, ClaimNotFoundException {
        claimsService.changeStatus(id, status);

        return ResponseEntity
                .noContent().build();
    }

    @GetMapping("/types")
    public List<ClaimTypeDTO> types() throws ClaimsServiceException {
        return claimsService.types();
    }

    @PostMapping("/{id}/type")
    public ResponseEntity<?> type(@PathVariable String id, @RequestBody @Valid ClaimTypeDTO dto)
            throws ClaimsServiceException, ClaimNotFoundException {
        claimsService.changeType(id, dto);

        return ResponseEntity
                .noContent().build();
    }

}
