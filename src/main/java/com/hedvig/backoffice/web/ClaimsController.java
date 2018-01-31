package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.claims.ClaimException;
import com.hedvig.backoffice.services.claims.ClaimStatus;
import com.hedvig.backoffice.services.claims.ClaimUpdateService;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.web.dto.ClaimDTO;
import com.hedvig.backoffice.web.dto.ClaimEventDTO;
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
    private final ClaimUpdateService updateService;

    @Autowired
    public ClaimsController(ClaimsService claimsService, ClaimUpdateService claimUpdateService) {
        this.claimsService = claimsService;
        this.updateService = claimUpdateService;
    }

    @GetMapping
    public List<ClaimDTO> claims() throws ClaimException {
        return claimsService.list();
    }

    @GetMapping("/{id}")
    public ClaimDTO claim(@PathVariable String id) throws ClaimException {
        return claimsService.find(id);
    }

    @PostMapping("/{id}/status/{status}")
    public ResponseEntity<?> status(@PathVariable String id, @PathVariable ClaimStatus status)
            throws ClaimException {
        updateService.changeStatus(id, status);

        return ResponseEntity
                .noContent().build();
    }

    @GetMapping("/types")
    public List<ClaimTypeDTO> types() throws ClaimException {
        return claimsService.types();
    }

    @PostMapping("/{id}/type")
    public ResponseEntity<?> type(@PathVariable String id, @RequestBody @Valid ClaimTypeDTO dto)
            throws ClaimException {
        updateService.changeType(id, dto);

        return ResponseEntity
                .noContent().build();
    }

    @GetMapping("/{id}/events")
    public List<ClaimEventDTO> events(@PathVariable String id) throws ClaimException {
        return claimsService.events(id);
    }

}
