package com.hedvig.backoffice.services.settings;

import com.hedvig.backoffice.domain.SystemSetting;
import com.hedvig.backoffice.domain.SystemSettingType;

public interface SystemSettingsService {

    SystemSetting getSetting(SystemSettingType type, String defaultValue);
    void saveSetting(SystemSetting setting);

}
