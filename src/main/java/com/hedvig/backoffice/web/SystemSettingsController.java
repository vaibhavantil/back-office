package com.hedvig.backoffice.web;

import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.security.GatekeeperUser;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.services.settings.SystemSettingsService;
import com.hedvig.backoffice.services.settings.dto.SystemSettingDTO;
import com.hedvig.backoffice.web.dto.MeDTO;
import com.hedvig.backoffice.web.dto.PersonnelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/settings")
public class SystemSettingsController {

  private final SystemSettingsService settingsService;
  private final PersonnelService personnelService;

  @Autowired
  public SystemSettingsController(SystemSettingsService settingsService,
                                  PersonnelService personnelService) {
    this.settingsService = settingsService;
    this.personnelService = personnelService;
  }

  @GetMapping
  public List<SystemSettingDTO> list() {
    return settingsService.list();
  }

  @PostMapping("/update")
  public ResponseEntity<?> update(@RequestBody SystemSettingDTO dto) {
    settingsService.update(dto.getType(), dto.getValue());
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/me")
  public MeDTO me(@AuthenticationPrincipal Authentication authentication)
    throws AuthorizationException {
    return MeDTO.from(personnelService.me(authentication.getName()), (GatekeeperUser) authentication.getPrincipal());
  }

  @PostMapping("/auth-success")
  public PersonnelDTO handleAuthSuccess(@AuthenticationPrincipal Principal principal) {
    return PersonnelDTO.fromDomain(personnelService.storeAuthentication(principal));
  }
}
