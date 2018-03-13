package com.hedvig.backoffice.web;

import com.hedvig.backoffice.config.feign.ExternalServiceException;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
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
    public List<Claim> claims() {
        return claimsService.list();
    }

    @GetMapping("/{id}")
    public Claim claim(@PathVariable String id) {
        return Optional.ofNullable(claimsService.find(id))
                .orElseThrow(() -> new ExternalServiceException("claim-service unavailable"));
    }

    @GetMapping("/user/{id}")
    public List<Claim> listByUserId(@PathVariable String id) {
        return claimsService.listByUserId(id);
    }

    @GetMapping("/types")
    public List<ClaimType> types() {
        return claimsService.types();
    }

    @PutMapping("/{id}/payments")
    public ResponseEntity<?> addPayment(@PathVariable String id, @RequestBody @Valid ClaimPayment dto) {
        dto.setClaimID(id);
        claimsService.addPayment(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/notes")
    public ResponseEntity<?> addNote(@PathVariable String id, @RequestBody ClaimNote dto)  {
        dto.setClaimID(id);
        claimsService.addNote(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/data")
    public ResponseEntity<?> addData(@PathVariable String id, @RequestBody ClaimData dto) {
        dto.setClaimID(id);
        claimsService.addData(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/state")
    public ResponseEntity<?> state(@PathVariable String id, @RequestBody @Valid ClaimStateUpdate state) {
        state.setClaimID(id);
        claimsService.changeState(state);
        return ResponseEntity
                .noContent().build();
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<?> reserve(@PathVariable String id, @RequestBody @Valid ClaimReserveUpdate reserve) {
        reserve.setClaimID(id);
        claimsService.changeReserve(reserve);
        return ResponseEntity
                .noContent().build();
    }

    @PostMapping("/{id}/type")
    public ResponseEntity<?> type(@PathVariable String id, @RequestBody @Valid ClaimTypeUpdate type) {
        type.setClaimID(id);
        claimsService.changeType(type);
        return ResponseEntity
                .noContent().build();
    }

    @GetMapping("/stat")
    public Map<String, Long> statistics() {
        return claimsService.statistics();
    }

}
