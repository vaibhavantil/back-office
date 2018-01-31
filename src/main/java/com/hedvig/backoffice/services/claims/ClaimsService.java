package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.web.dto.ClaimDTO;
import com.hedvig.backoffice.web.dto.ClaimEventDTO;
import com.hedvig.backoffice.web.dto.ClaimTypeDTO;

import java.util.List;

public interface ClaimsService {

    List<ClaimDTO> list() throws ClaimException;
    ClaimDTO find(String id) throws ClaimException;
    List<ClaimTypeDTO> types() throws ClaimException;
    void save(ClaimDTO dto) throws ClaimException;
    List<ClaimEventDTO> events(String id) throws ClaimException;
    void addEvent(ClaimEventDTO dto) throws ClaimException;

}
