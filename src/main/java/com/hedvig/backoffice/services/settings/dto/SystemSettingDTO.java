package com.hedvig.backoffice.services.settings.dto;

import com.hedvig.backoffice.domain.SystemSetting;
import com.hedvig.backoffice.domain.SystemSettingType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SystemSettingDTO {

  private SystemSettingType type;
  private String value;

  public static SystemSettingDTO fromDomain(SystemSetting setting) {
    return new SystemSettingDTO(setting.getType(), setting.getValue());
  }
}
