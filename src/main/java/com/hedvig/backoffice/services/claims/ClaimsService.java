package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.web.dto.claims.ClaimDTO;
import com.hedvig.backoffice.web.dto.claims.ClaimEventDTO;
import com.hedvig.backoffice.web.dto.claims.ClaimNoteDTO;
import com.hedvig.backoffice.web.dto.claims.ClaimPayoutDTO;
import com.hedvig.backoffice.web.dto.claims.ClaimTypeDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ClaimsService {

    List<ClaimDTO> list() throws ClaimException;
    ClaimDTO find(String id) throws ClaimException;
    List<ClaimTypeDTO> types() throws ClaimException;
    void save(ClaimDTO dto) throws ClaimException;
    List<ClaimEventDTO> events(String id) throws ClaimException;
    void addEvent(ClaimEventDTO dto) throws ClaimException;
    List<ClaimPayoutDTO> payouts(String id) throws ClaimException;
    void addPayout(ClaimPayoutDTO dto) throws ClaimException;
    void removePayout(String id, String claimId) throws ClaimException;
    List<ClaimNoteDTO> notes(String id) throws ClaimException;
    void addNote(ClaimNoteDTO dto) throws ClaimException;
    void removeNote(String id, String claimId) throws ClaimException;
    void changeType(String id, ClaimTypeDTO dto) throws ClaimException;
    void changeStatus(String id, ClaimStatus status) throws ClaimException;
    void setResume(String id, BigDecimal resume) throws ClaimException;

}
