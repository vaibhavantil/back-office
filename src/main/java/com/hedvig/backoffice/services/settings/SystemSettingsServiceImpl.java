package com.hedvig.backoffice.services.settings;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hedvig.backoffice.domain.SystemSetting;
import com.hedvig.backoffice.domain.SystemSettingType;
import com.hedvig.backoffice.repository.SystemSettingRepository;
import com.hedvig.backoffice.services.settings.dto.SystemSettingDTO;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemSettingsServiceImpl implements SystemSettingsService {

  private final SystemSettingRepository systemSettingRepository;
  private final String accessToken;

  @Autowired
  public SystemSettingsServiceImpl(
      SystemSettingRepository systemSettingRepository, @Value("${oauth.secret:}") String jwtSecret)
      throws UnsupportedEncodingException {
    this.systemSettingRepository = systemSettingRepository;

    Algorithm algorithm =
        StringUtils.isBlank(jwtSecret) ? Algorithm.none() : Algorithm.HMAC256(jwtSecret);
    accessToken = JWT.create().withIssuer("hedvig-internal").sign(algorithm);
  }

  @Override
  @Transactional
  public synchronized SystemSetting getSetting(SystemSettingType type, String defaultValue) {
    return systemSettingRepository
        .findByType(type)
        .orElseGet(
            () -> {
              SystemSetting s = new SystemSetting(type, defaultValue);
              systemSettingRepository.save(s);
              return s;
            });
  }

  @Override
  public List<SystemSettingDTO> list() {
    return systemSettingRepository
        .findAll()
        .stream()
        .map(SystemSettingDTO::fromDomain)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void update(SystemSettingType type, String value) {
    SystemSetting setting =
        systemSettingRepository.findByType(type).orElseGet(() -> new SystemSetting(type, value));

    setting.setValue(value);
    systemSettingRepository.save(setting);
  }

  @Override
  public String getInternalAccessToken() {
    return accessToken;
  }
}
