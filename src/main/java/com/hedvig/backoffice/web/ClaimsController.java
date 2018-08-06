package com.hedvig.backoffice.web;

import com.hedvig.backoffice.config.feign.ExternalServiceException;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.dto.Claim;
import com.hedvig.backoffice.services.claims.dto.ClaimData;
import com.hedvig.backoffice.services.claims.dto.ClaimNote;
import com.hedvig.backoffice.services.claims.dto.ClaimPayment;
import com.hedvig.backoffice.services.claims.dto.ClaimReserveUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimStateUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimType;
import com.hedvig.backoffice.services.claims.dto.ClaimTypeUpdate;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  @GetMapping
  public List<Claim> claims(@AuthenticationPrincipal Principal principal) {
    return claimsService.list(personnelService.getIdToken(principal.getName()));
  }

  @GetMapping("/{id}")
  public Claim claim(@PathVariable String id, @AuthenticationPrincipal Principal principal) {
    return Optional.ofNullable(
            claimsService.find(id, personnelService.getIdToken(principal.getName())))
        .orElseThrow(() -> new ExternalServiceException("claim-service unavailable"));
  }

  @GetMapping("/user/{id}")
  public List<Claim> listByUserId(
      @PathVariable String id, @AuthenticationPrincipal Principal principal) {
    return claimsService.listByUserId(id, personnelService.getIdToken(principal.getName()));
  }

  @GetMapping("/types")
  public List<ClaimType> types(@AuthenticationPrincipal Principal principal) {
    return claimsService.types(personnelService.getIdToken(principal.getName()));
  }

  @PutMapping("/{id}/payments")
  public ResponseEntity<?> addPayment(
      @PathVariable String id,
      @RequestBody @Valid ClaimPayment dto,
      @AuthenticationPrincipal Principal principal) {
    dto.setClaimID(id);
    claimsService.addPayment(dto, personnelService.getIdToken(principal.getName()));
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/notes")
  public ResponseEntity<?> addNote(
      @PathVariable String id,
      @RequestBody ClaimNote dto,
      @AuthenticationPrincipal Principal principal) {
    dto.setClaimID(id);
    claimsService.addNote(dto, personnelService.getIdToken(principal.getName()));
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/data")
  public ResponseEntity<?> addData(
      @PathVariable String id,
      @RequestBody ClaimData dto,
      @AuthenticationPrincipal Principal principal) {
    dto.setClaimID(id);
    claimsService.addData(dto, personnelService.getIdToken(principal.getName()));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{id}/state")
  public ResponseEntity<?> state(
      @PathVariable String id,
      @RequestBody @Valid ClaimStateUpdate state,
      @AuthenticationPrincipal Principal principal) {
    state.setClaimID(id);
    claimsService.changeState(state, personnelService.getIdToken(principal.getName()));
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/reserve")
  public ResponseEntity<?> reserve(
      @PathVariable String id,
      @RequestBody @Valid ClaimReserveUpdate reserve,
      @AuthenticationPrincipal Principal principal) {
    reserve.setClaimID(id);
    claimsService.changeReserve(reserve, personnelService.getIdToken(principal.getName()));
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/type")
  public ResponseEntity<?> type(
      @PathVariable String id,
      @RequestBody @Valid ClaimTypeUpdate type,
      @AuthenticationPrincipal Principal principal) {
    type.setClaimID(id);
    claimsService.changeType(type, personnelService.getIdToken(principal.getName()));
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/stat")
  public Map<String, Long> statistics(@AuthenticationPrincipal Principal principal) {
    return claimsService.statistics(personnelService.getIdToken(principal.getName()));
  }
}
