package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.claims.dto.*;

import java.math.BigDecimal;
import java.util.List;

public interface ClaimsService {

    List<Claim> list();
    Claim find(String id) throws ClaimException;

    List<ClaimType> types();

    boolean addPayout(ClaimPayment dto) throws ClaimException;
    boolean addNote(ClaimNote dto) throws ClaimException;
    boolean addData(ClaimData data) throws ClaimException;

    boolean changeState(String id, ClaimState status) throws ClaimException;
    boolean changeReserve(String id, BigDecimal value) throws ClaimException;
    boolean changeType(String id, String type) throws ClaimException;

}
