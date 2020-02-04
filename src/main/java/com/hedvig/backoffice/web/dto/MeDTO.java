package com.hedvig.backoffice.web.dto;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.security.GatekeeperUser;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
public class MeDTO {
  private String id;
  private String email;
  private List<String> scopes;

  public static MeDTO from(final Personnel personnel, final GatekeeperUser principalUser) {
    final List<String> scopes = principalUser.getAuthorities().stream()
      .map(GrantedAuthority::getAuthority)
      .collect(toList());
    return new MeDTO(personnel.getId(), personnel.getEmail(), scopes);
  }
}
