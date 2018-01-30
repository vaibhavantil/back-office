package com.hedvig.backoffice.services.settings;

import com.hedvig.backoffice.domain.SystemSetting;
import com.hedvig.backoffice.domain.SystemSettingType;
import com.hedvig.backoffice.repository.SystemSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemSettingsServiceImpl implements SystemSettingsService {

    private final SystemSettingRepository systemSettingRepository;

    @Autowired
    public SystemSettingsServiceImpl(SystemSettingRepository systemSettingRepository) {
        this.systemSettingRepository = systemSettingRepository;
    }

    @Override
    public synchronized SystemSetting getSetting(SystemSettingType type, String defaultValue) {
        return systemSettingRepository
                .findByType(type)
                .orElseGet(() -> {
                    SystemSetting s = new SystemSetting(SystemSettingType.BOT_SERVICE_LAST_TIMESTAMP, defaultValue);
                    systemSettingRepository.save(s);
                    return s;
                });
    }

    @Override
    public synchronized void saveSetting(SystemSetting setting) {
        systemSettingRepository.save(setting);
    }
}
