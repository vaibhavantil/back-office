package com.hedvig.backoffice.services.personnel;

import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.web.dto.PersonnelDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface PersonnelService {

    void processAuthorization(Authentication auth);
    List<PersonnelDTO> list();
    PersonnelDTO me(String id) throws AuthorizationException;

}
