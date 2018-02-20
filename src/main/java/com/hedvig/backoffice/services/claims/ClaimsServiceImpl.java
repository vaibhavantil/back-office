package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.claims.dto.*;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClaimsServiceImpl implements ClaimsService {

    private static Logger logger = LoggerFactory.getLogger(ClaimsServiceImpl.class);

    private final String baseUrl;
    private final String claims;
    private final String claimById;
    private final String claimTypes;
    private final String paymentUrl;
    private final String noteUrl;
    private final String dataUrl;
    private final String stateUrl;
    private final String reserveUrl;
    private final String typeUrl;

    private final RestTemplate template;

    @Autowired
    public ClaimsServiceImpl(@Value("${claims.baseUrl}") String baseUrl,
                             @Value("${claims.urls.claims}") String claims,
                             @Value("${claims.urls.claimById}") String claimById,
                             @Value("${claims.urls.claimTypes}") String claimTypes,
                             @Value("${claims.urls.payment}") String paymentUrl,
                             @Value("${claims.urls.note}") String noteUrl,
                             @Value("${claims.urls.data}") String dataUrl,
                             @Value("${claims.urls.state}") String stateUrl,
                             @Value("${claims.urls.reserve}") String reserveUrl,
                             @Value("${claims.urls.type}") String typeUrl) {

        this.baseUrl = baseUrl;
        this.claims = claims;
        this.claimById = claimById;
        this.claimTypes = claimTypes;
        this.paymentUrl = paymentUrl;
        this.noteUrl = noteUrl;
        this.dataUrl = dataUrl;
        this.stateUrl = stateUrl;
        this.reserveUrl = reserveUrl;
        this.typeUrl = typeUrl;

        this.template = new RestTemplate();

        logger.info("CLAIMS SERVICE:");
        logger.info("class: " + ClaimsServiceImpl.class.getName());
        logger.info("base: " + baseUrl);
        logger.info("claims: " + claims);
        logger.info("id: " + claimById);
        logger.info("types: " + claimTypes);
        logger.info("payment: " + paymentUrl);
        logger.info("note: " + noteUrl);
        logger.info("data: " + dataUrl);
        logger.info("state: " + stateUrl);
        logger.info("reserve: " + reserveUrl);
        logger.info("type: " + typeUrl);
    }

    @HystrixCommand(fallbackMethod = "listFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION)
    @Override
    public List<Claim> list() {
        ResponseEntity<Claim[]> response = template.getForEntity(baseUrl + claims, Claim[].class);
        return Arrays.stream(response.getBody())
                .collect(Collectors.toList());
    }

    public List<Claim> listFallback(Throwable t) {
        logger.error("failed claims fetching", t);
        return null;
    }

    @HystrixCommand(fallbackMethod = "findFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION,
            ignoreExceptions = { ClaimBadRequestException.class })
    @Override
    public Claim find(String id) throws ClaimException {
        try {
            ResponseEntity<Claim> response = template.getForEntity(baseUrl + claimById + "?claimID=" + id, Claim.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new ClaimBadRequestException(e);
        }
    }

    private Claim findFallback(String id, Throwable t) {
        logger.error("failed claim fetching", t);
        return null;
    }

    @HystrixCommand(fallbackMethod = "typesFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION)
    @Override
    public List<ClaimType> types() {
        ResponseEntity<ClaimType[]> response = template.getForEntity(baseUrl + claimTypes, ClaimType[].class);
        return Arrays.asList(response.getBody());
    }

    private List<ClaimType> typesFallback(Throwable t) {
        logger.error("failed claim types fetching", t);
        return null;
    }

    @HystrixCommand(fallbackMethod = "addPaymentFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION,
            ignoreExceptions = ClaimBadRequestException.class)
    @Override
    public boolean addPayment(ClaimPayment dto) throws ClaimException {
        try {
            template.postForEntity(baseUrl + paymentUrl, dto, Void.class);
        } catch (HttpClientErrorException e) {
            throw new ClaimBadRequestException(e);
        }
        return true;
    }

    public boolean addPaymentFallback(ClaimPayment dto, Throwable t) {
        logger.error("failed add payment", t);
        return false;
    }

    @HystrixCommand(fallbackMethod = "addNoteFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION,
            ignoreExceptions = ClaimBadRequestException.class)
    @Override
    public boolean addNote(ClaimNote dto) throws ClaimException {
        try {
            template.postForEntity(baseUrl + noteUrl, dto, Void.class);
        } catch (HttpClientErrorException e) {
            throw new ClaimBadRequestException(e);
        }
        return true;
    }

    private boolean addNoteFallback(ClaimNote dto, Throwable t) {
        logger.error("failed add note", t);
        return false;
    }

    @HystrixCommand(fallbackMethod = "addDataFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION,
            ignoreExceptions = ClaimBadRequestException.class)
    @Override
    public boolean addData(ClaimData data) throws ClaimException {
        try {
            template.postForEntity(baseUrl + dataUrl, data, Void.class);
        } catch (HttpClientErrorException e) {
            throw new ClaimBadRequestException(e);
        }
        return true;
    }

    private boolean addDataFallback(ClaimData dto, Throwable t) {
        logger.error("failed add data", t);
        return false;
    }

    @HystrixCommand(fallbackMethod = "changeStateFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION,
            ignoreExceptions = ClaimBadRequestException.class)
    @Override
    public boolean changeState(ClaimStateUpdate state) throws ClaimException {
        try {
            template.postForEntity(baseUrl + stateUrl, state, Void.class);
        } catch (HttpClientErrorException e) {
            throw new ClaimBadRequestException(e);
        }
        return true;
    }

    private boolean changeStateFallback(ClaimStateUpdate state, Throwable t) {
        logger.error("failed update state", t);
        return false;
    }

    @HystrixCommand(fallbackMethod = "changeReserveFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION,
            ignoreExceptions = ClaimBadRequestException.class)
    @Override
    public boolean changeReserve(ClaimReserveUpdate reserve) throws ClaimException {
        try {
            template.postForEntity(baseUrl + reserveUrl, reserve, Void.class);
        } catch (HttpClientErrorException e) {
            throw new ClaimBadRequestException(e);
        }
        return true;
    }

    private boolean changeReserveFallback(ClaimReserveUpdate reserve, Throwable t) {
        logger.error("failed update reserve", t);
        return false;
    }

    @HystrixCommand(fallbackMethod = "changeTypeFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION,
            ignoreExceptions = ClaimBadRequestException.class)
    @Override
    public boolean changeType(ClaimTypeUpdate type) throws ClaimException {
        try {
            template.postForEntity(baseUrl + typeUrl, type, Void.class);
        } catch (HttpClientErrorException e) {
            throw new ClaimBadRequestException(e);
        }
        return true;
    }

    private boolean changeTypeFallback(ClaimTypeUpdate type, Throwable t) {
        logger.error("failed update type", t);
        return false;
    }

}
