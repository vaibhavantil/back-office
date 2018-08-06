package com.hedvig.backoffice.services.settings;

import com.hedvig.backoffice.domain.SystemSetting;
import com.hedvig.backoffice.domain.SystemSettingType;
import com.hedvig.backoffice.services.settings.dto.SystemSettingDTO;
import java.util.List;

public interface SystemSettingsService {

  SystemSetting getSetting(SystemSettingType type, String defaultValue);

  List<SystemSettingDTO> list();

  void update(SystemSettingType type, String value);

  String getInternalAccessToken();
}
