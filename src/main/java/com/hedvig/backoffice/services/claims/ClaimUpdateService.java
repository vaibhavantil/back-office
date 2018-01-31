package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.web.dto.ClaimTypeDTO;

public interface ClaimUpdateService {

    void changeType(String id, ClaimTypeDTO dto) throws ClaimException;
    void changeStatus(String id, ClaimStatus status) throws ClaimException;

}
