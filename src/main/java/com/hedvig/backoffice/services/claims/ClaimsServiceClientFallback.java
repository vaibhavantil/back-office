package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.claims.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ClaimsServiceClientFallback implements ClaimsServiceClient {

    private static Logger log = LoggerFactory.getLogger(ClaimsServiceClientFallback.class);

    @Override
    public Map<String, Long> statistics() {
        log.error("request to claim-service failed");

        Map<String, Long> stat = new HashMap<>();
        for (ClaimState state : ClaimState.values()) {
            stat.put(state.name(), 0L);
        }

        return stat;
    }

    @Override
    public List<Claim> listByUserId(String userId) {
        log.error("request to claim-service failed");
        return new ArrayList<>();
    }

    @Override
    public List<Claim> list() {
        log.error("request to claim-service failed");
        return new ArrayList<>();
    }

    @Override
    public Claim find(String id) {
        log.error("request to claim-service failed");
        return null;
    }

    @Override
    public List<ClaimType> types() {
        log.error("request to claim-service failed");
        return new ArrayList<>();
    }

    @Override
    public void addPayment(ClaimPayment dto) {
        log.error("request to claim-service failed");
    }

    @Override
    public void addNote(ClaimNote dto) {
        log.error("request to claim-service failed");
    }

    @Override
    public void addDataItem(ClaimData data) {
        log.error("request to claim-service failed");
    }

    @Override
    public void updateState(ClaimStateUpdate state) {
        log.error("request to claim-service failed");
    }

    @Override
    public void updateReserve(ClaimReserveUpdate reserve) {
        log.error("request to claim-service failed");
    }

    @Override
    public void updateType(ClaimTypeUpdate type) {
        log.error("request to claim-service failed");
    }

}
