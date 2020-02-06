package com.hedvig.backoffice.services.personnel;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.web.dto.PersonnelDTO;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

public interface PersonnelService {

  @Transactional
  Personnel storeAuthentication(Principal auth);

  Personnel me(String email) throws AuthorizationException;

  Personnel getPersonnelByEmail(String email) throws AuthorizationException;

  String getIdToken(String id);
}
