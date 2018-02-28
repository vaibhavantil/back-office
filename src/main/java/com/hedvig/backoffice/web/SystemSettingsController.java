package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.settings.SystemSettingsService;
import com.hedvig.backoffice.services.settings.dto.SystemSettingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
public class SystemSettingsController {

    private final SystemSettingsService settingsService;

    @Autowired
    public SystemSettingsController(SystemSettingsService settingsService) {
        this.settingsService = settingsService;
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

}
