package com.hedvig.backoffice.services.settings;

import com.hedvig.backoffice.domain.SystemSetting;
import com.hedvig.backoffice.domain.SystemSettingType;
import com.hedvig.backoffice.repository.SystemSettingRepository;
import com.hedvig.backoffice.services.settings.dto.SystemSettingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystemSettingsServiceImpl implements SystemSettingsService {

    private final SystemSettingRepository systemSettingRepository;

    @Autowired
    public SystemSettingsServiceImpl(SystemSettingRepository systemSettingRepository) {
        this.systemSettingRepository = systemSettingRepository;
    }

    @Override
    @Transactional
    public synchronized SystemSetting getSetting(SystemSettingType type, String defaultValue) {
        return systemSettingRepository
                .findByType(type)
                .orElseGet(() -> {
                    SystemSetting s = new SystemSetting(type, defaultValue);
                    systemSettingRepository.save(s);
                    return s;
                });
    }

    @Override
    public List<SystemSettingDTO> list() {
        return systemSettingRepository.findAll()
                .stream()
                .map(SystemSettingDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void update(SystemSettingType type, String value) {
        SystemSetting setting = systemSettingRepository.findByType(type)
                .orElseGet(() -> new SystemSetting(type, value));

        setting.setValue(value);
        systemSettingRepository.save(setting);
    }

}
