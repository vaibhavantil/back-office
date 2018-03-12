package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.claims.dto.*;

import java.util.List;
import java.util.Map;

public interface ClaimsService {

    List<Claim> list();
    List<Claim> listByUserId(String userId);
    Claim find(String id) throws ClaimException;

    List<ClaimType> types();

    boolean addPayment(ClaimPayment dto) throws ClaimException;
    boolean addNote(ClaimNote dto) throws ClaimException;
    boolean addData(ClaimData data) throws ClaimException;

    boolean changeState(ClaimStateUpdate state) throws ClaimException;
    boolean changeReserve(ClaimReserveUpdate reserve) throws ClaimException;
    boolean changeType(ClaimTypeUpdate type) throws ClaimException;

    Map<String, Long> statistics();

}
