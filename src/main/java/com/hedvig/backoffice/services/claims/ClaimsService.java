package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.web.dto.ClaimDTO;

import java.util.List;

public interface ClaimsService {

    List<ClaimDTO> list() throws ClaimsServiceException;
    ClaimDTO find(String id) throws ClaimsServiceException, ClaimNotFoundException;

}
