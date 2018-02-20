package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.claims.dto.*;

import java.util.List;

public interface ClaimsService {

    List<Claim> list();
    Claim find(String id) throws ClaimException;

    List<ClaimType> types();

    boolean addPayment(ClaimPayment dto) throws ClaimException;
    boolean addNote(ClaimNote dto) throws ClaimException;
    boolean addData(ClaimData data) throws ClaimException;

    boolean changeState(ClaimState state) throws ClaimException;
    boolean changeReserve(ClaimReserve reserve) throws ClaimException;
    boolean changeType(String id, String type) throws ClaimException;

}
