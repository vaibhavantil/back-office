package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.web.dto.ClaimDTO;
import com.hedvig.backoffice.web.dto.ClaimTypeDTO;

import java.util.List;

public interface ClaimsService {

    List<ClaimDTO> list() throws ClaimsServiceException;
    ClaimDTO find(String id) throws ClaimsServiceException, ClaimNotFoundException;
    void changeStatus(String id, ClaimStatus status) throws ClaimsServiceException, ClaimNotFoundException;
    List<ClaimTypeDTO> types() throws ClaimsServiceException;
    void changeType(String id, ClaimTypeDTO dto) throws ClaimsServiceException, ClaimNotFoundException;

}
