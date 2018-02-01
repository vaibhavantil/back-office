package com.hedvig.backoffice.web;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.claims.ClaimException;
import com.hedvig.backoffice.services.claims.ClaimStatus;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.web.dto.claims.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/claims")
public class ClaimsController {

    private final ClaimsService claimsService;
    private final PersonnelRepository personnelRepository;

    @Autowired
    public ClaimsController(ClaimsService claimsService, PersonnelRepository personnelRepository) {
        this.claimsService = claimsService;
        this.personnelRepository = personnelRepository;
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
        claimsService.changeStatus(id, status);

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
        claimsService.changeType(id, dto);

        return ResponseEntity
                .noContent().build();
    }

    @GetMapping("/{id}/events")
    public List<ClaimEventDTO> events(@PathVariable String id) throws ClaimException {
        return claimsService.events(id);
    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<?> resume(@PathVariable String id, @RequestBody @Valid ClaimResumeDTO resume)
            throws ClaimException {
        claimsService.setResume(id, resume.getResume());

        return ResponseEntity
                .noContent().build();
    }

    @GetMapping("/{id}/payouts")
    public List<ClaimPayoutDTO> payouts(@PathVariable String id) throws ClaimException {
        return claimsService.payouts(id);
    }

    @PutMapping("/{id}/payouts")
    public ResponseEntity<?> addPayout(@PathVariable String id, @RequestBody @Valid ClaimPayoutDTO dto) throws ClaimException {
        dto.setId(UUID.randomUUID().toString());
        dto.setClaimId(id);
        dto.setDate(new Date().toInstant());

        claimsService.addPayout(dto);

        return ResponseEntity
                .noContent().build();
    }

    @DeleteMapping("/{id}/payouts/{payoutId}")
    public ResponseEntity<?> removePayout(@PathVariable String id, @PathVariable String payoutId) throws ClaimException {
        claimsService.removePayout(payoutId, id);

        return ResponseEntity
                .noContent().build();
    }

    @GetMapping("/{id}/notes")
    public List<ClaimNoteDTO> claims(@PathVariable String id) throws ClaimException {
        return claimsService.notes(id);
    }

    @PutMapping("/{id}/notes")
    public ResponseEntity<?> addNote(@PathVariable String id, @RequestBody @Valid ClaimNoteDTO dto,
                                     @AuthenticationPrincipal String principal) throws ClaimException, AuthorizationException {
        Personnel personnel = personnelRepository.findByEmail(principal).orElseThrow(AuthorizationException::new);

        dto.setId(UUID.randomUUID().toString());
        dto.setClaimId(id);
        dto.setAdminId(personnel.getId());
        dto.setDate(new Date().toInstant());

        claimsService.addNote(dto);

        return ResponseEntity
                .noContent().build();
    }

    @DeleteMapping("/{id}/notes/{noteId}")
    public ResponseEntity<?> removeNote(@PathVariable String id, @PathVariable String noteId) throws ClaimException {
        claimsService.removeNote(noteId, id);

        return ResponseEntity
                .noContent().build();
    }
}
