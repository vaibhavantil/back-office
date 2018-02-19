package com.hedvig.backoffice.web;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.claims.ClaimException;
import com.hedvig.backoffice.services.claims.ClaimStatus;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.ClaimsServiceException;
import com.hedvig.backoffice.web.dto.claims.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        return Optional.ofNullable(claimsService.list())
                .orElseThrow(ClaimsServiceException::new);
    }

    @GetMapping("/{id}")
    public ClaimDTO claim(@PathVariable String id) throws ClaimException {
        return Optional.ofNullable(claimsService.find(id))
                .orElseThrow(ClaimsServiceException::new);
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
        return Optional.ofNullable(claimsService.types())
                .orElseThrow(ClaimsServiceException::new);
    }

    @PostMapping("/{id}/type/{type}")
    public ResponseEntity<?> type(@PathVariable String id, @PathVariable String type)
            throws ClaimException {
        claimsService.changeType(id, type);

        return ResponseEntity
                .noContent().build();
    }

    @PostMapping("/{id}/details")
    public ResponseEntity<?> details(@PathVariable String id, @RequestBody @Valid ClaimDetailsDTO dto) throws ClaimException {
        claimsService.addDetails(id, dto);

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
    public ResponseEntity<ClaimPayoutDTO> addPayout(@PathVariable String id, @RequestBody @Valid ClaimPayoutDTO dto) throws ClaimException {
        dto.setClaimId(id);
        dto.setDate(new Date().toInstant());

        ClaimPayoutDTO result = claimsService.addPayout(dto);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/payouts/{payoutId}")
    public ResponseEntity<?> updatePayout(@PathVariable String id,
                                                       @PathVariable String payoutId,
                                                       @RequestBody ClaimPayoutDTO dto) throws ClaimException {
        dto.setClaimId(id);
        dto.setId(payoutId);

        claimsService.updatePayout(dto);

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
    public ResponseEntity<ClaimNoteDTO> addNote(@PathVariable String id, @RequestBody @Valid ClaimNoteDTO dto,
                                     @AuthenticationPrincipal String principal) throws ClaimException, AuthorizationException {
        Personnel personnel = personnelRepository.findByEmail(principal).orElseThrow(AuthorizationException::new);

        dto.setClaimId(id);
        dto.setAdminId(personnel.getId());
        dto.setDate(new Date().toInstant());

        ClaimNoteDTO result = claimsService.addNote(dto);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/notes/{noteId}")
    public ResponseEntity<?> removeNote(@PathVariable String id, @PathVariable String noteId) throws ClaimException {
        claimsService.removeNote(noteId, id);

        return ResponseEntity
                .noContent().build();
    }
}
