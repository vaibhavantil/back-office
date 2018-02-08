package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.web.dto.claims.*;

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
    ClaimPayoutDTO addPayout(ClaimPayoutDTO dto) throws ClaimException;
    void removePayout(String id, String claimId) throws ClaimException;
    List<ClaimNoteDTO> notes(String id) throws ClaimException;
    ClaimNoteDTO addNote(ClaimNoteDTO dto) throws ClaimException;
    void removeNote(String id, String claimId) throws ClaimException;
    void changeType(String id, String type) throws ClaimException;
    void changeStatus(String id, ClaimStatus status) throws ClaimException;
    void setResume(String id, BigDecimal resume) throws ClaimException;
    void addDetails(String id, ClaimDetailsDTO dto) throws ClaimException;

}
