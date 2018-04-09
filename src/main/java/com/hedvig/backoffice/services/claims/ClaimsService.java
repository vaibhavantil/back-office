package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.claims.dto.*;

import java.util.List;
import java.util.Map;

public interface ClaimsService {

    List<Claim> list(String token);
    List<Claim> listByUserId(String userId, String token);
    Claim find(String id, String token);

    List<ClaimType> types(String token);

    void addPayment(ClaimPayment dto, String token);
    void addNote(ClaimNote dto, String token);
    void addData(ClaimData data, String token);

    void changeState(ClaimStateUpdate state, String token);
    void changeReserve(ClaimReserveUpdate reserve, String token);
    void changeType(ClaimTypeUpdate type, String token);

    Map<String, Long> statistics(String token);
    long totalClaims(String token);

}
