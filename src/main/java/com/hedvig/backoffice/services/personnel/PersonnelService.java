package com.hedvig.backoffice.services.personnel;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.web.dto.PersonnelDTO;

import java.util.List;

public interface PersonnelService {

    Personnel authorize(String email, String password) throws AuthorizationException;
    void register(PersonnelDTO dto) throws PersonnelServiceException;
    void remove(long id) throws PersonnelServiceException;
    void update(long id, PersonnelDTO dto) throws PersonnelServiceException;
    List<PersonnelDTO> list();

}
