package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.claims.ClaimException;
import com.hedvig.backoffice.services.claims.ClaimState;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.ClaimsServiceException;
import com.hedvig.backoffice.services.claims.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/claims")
public class ClaimsController {

    private final ClaimsService claimsService;

    @Autowired
    public ClaimsController(ClaimsService claimsService) {
        this.claimsService = claimsService;
    }

    @GetMapping
    public List<Claim> claims() throws ClaimException {
        return Optional.ofNullable(claimsService.list())
                .orElseThrow(ClaimsServiceException::new);
    }

    @GetMapping("/{id}")
    public Claim claim(@PathVariable String id) throws ClaimException {
        return Optional.ofNullable(claimsService.find(id))
                .orElseThrow(ClaimsServiceException::new);
    }

    @GetMapping("/types")
    public List<ClaimType> types() throws ClaimException {
        return Optional.ofNullable(claimsService.types())
                .orElseThrow(ClaimsServiceException::new);
    }

    @PutMapping("/{id}/payments")
    public ResponseEntity<?> addPayment(@PathVariable String id, @RequestBody @Valid ClaimPayment dto) throws ClaimException {
        dto.setClaimID(id);

        if (!claimsService.addPayment(dto)) {
            throw new ClaimsServiceException();
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/notes")
    public ResponseEntity<?> addNote(@PathVariable String id, @RequestBody ClaimNote dto) throws ClaimException {
        dto.setClaimID(id);

        if(!claimsService.addNote(dto)) {
            throw new ClaimsServiceException();
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/data")
    public ResponseEntity<?> addData(@PathVariable String id, @RequestBody ClaimData dto) throws ClaimException {
        dto.setClaimID(id);

        if (!claimsService.addData(dto)) {
            throw new ClaimsServiceException();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/status/{status}")
    public ResponseEntity<?> state(@PathVariable String id, @PathVariable ClaimState status)
            throws ClaimException {
        if (!claimsService.changeState(id, status)) {
            throw new ClaimsServiceException();
        }

        return ResponseEntity
                .noContent().build();
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<?> reserve(@PathVariable String id, @RequestBody @Valid ClaimReserve resume) throws ClaimException {
        if (!claimsService.changeReserve(id, resume.getReserve())) {
            throw new ClaimsServiceException();
        }

        return ResponseEntity
                .noContent().build();
    }

    @PostMapping("/{id}/type/{type}")
    public ResponseEntity<?> type(@PathVariable String id, @PathVariable String type) throws ClaimException {
        if (!claimsService.changeType(id, type)) {
            throw new ClaimsServiceException();
        }

        return ResponseEntity
                .noContent().build();
    }

}
