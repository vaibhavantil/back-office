package com.hedvig.backoffice.services.personnel;

import org.springframework.security.core.Authentication;

public interface PersonnelService {

    void processAuthorization(Authentication auth);

}
