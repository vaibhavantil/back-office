package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.claims.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

public class ClaimsServiceImpl implements ClaimsService {

    private static Logger logger = LoggerFactory.getLogger(ClaimsServiceImpl.class);

    private final ClaimsServiceClient client;

    @Autowired
    public ClaimsServiceImpl(@Value("${claims.baseUrl}") String baseUrl, ClaimsServiceClient client) {
        this.client = client;

        logger.info("CLAIMS SERVICE:");
        logger.info("class: " + ClaimsServiceImpl.class.getName());
        logger.info("base url: " + baseUrl);
    }

    @Override
    public List<Claim> list() {
        return client.list();
    }

    @Override
    public List<Claim> listByUserId(String userId) {
        return client.listByUserId(userId);
    }

    @Override
    public Claim find(String id) {
        return client.find(id);
    }

    @Override
    public List<ClaimType> types() {
        return client.types();
    }

    @Override
    public void addPayment(ClaimPayment dto) {
        client.addPayment(dto);
    }

    @Override
    public void addNote(ClaimNote dto) {
        client.addNote(dto);
    }

    @Override
    public void addData(ClaimData data) {
       client.addDataItem(data);
    }

    @Override
    public void changeState(ClaimStateUpdate state) {
        client.updateState(state);
    }

    @Override
    public void changeReserve(ClaimReserveUpdate reserve) {
        client.updateReserve(reserve);
    }

    @Override
    public void changeType(ClaimTypeUpdate type) {
        client.updateType(type);
    }

    @Override
    public Map<String, Long> statistics() {
        return client.statistics();
    }

}
