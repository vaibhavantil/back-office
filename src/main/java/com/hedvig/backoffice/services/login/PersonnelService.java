package com.hedvig.backoffice.services.login;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.security.AuthorizationException;

public interface PersonnelService {

    Personnel authorizeUser(String email, String password) throws AuthorizationException;

}
