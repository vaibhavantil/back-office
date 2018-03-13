package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.claims.dto.*;

import java.util.List;
import java.util.Map;

public interface ClaimsService {

    List<Claim> list();
    List<Claim> listByUserId(String userId);
    Claim find(String id);

    List<ClaimType> types();

    void addPayment(ClaimPayment dto);
    void addNote(ClaimNote dto);
    void addData(ClaimData data);

    void changeState(ClaimStateUpdate state);
    void changeReserve(ClaimReserveUpdate reserve);
    void changeType(ClaimTypeUpdate type);

    Map<String, Long> statistics();
    long totalClaims();

}
