package com.hedvig.backoffice.services.personnel;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.web.dto.PersonnelDTO;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface PersonnelService {

  void processAuthorization(Authentication auth);

  List<PersonnelDTO> list();

  PersonnelDTO me(String id) throws AuthorizationException;

  Personnel getPersonnel(String id) throws AuthorizationException;

  String getIdToken(String id);

  String getIdToken(Personnel personnel);
}
