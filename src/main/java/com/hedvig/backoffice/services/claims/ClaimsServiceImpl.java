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

    private final RestTemplate template;
    private final ClaimsServiceConfig config;

    @Autowired
    public ClaimsServiceImpl(ClaimsServiceConfig config) {

        this.config = config;
        this.template = new RestTemplate();

        logger.info("CLAIMS SERVICE:");
        logger.info("class: " + ClaimsServiceImpl.class.getName());
        logger.info(config.toString());
    }

    @HystrixCommand(fallbackMethod = "listFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION)
    @Override
    public List<Claim> list() {
        ResponseEntity<Claim[]> response = template.getForEntity(config.getBaseUrl() + config.getUrls().getClaims(), Claim[].class);
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
            ResponseEntity<Claim> response = template.getForEntity(config.getBaseUrl() + config.getUrls().getClaimById() + "?claimID=" + id, Claim.class);
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
        ResponseEntity<ClaimType[]> response = template.getForEntity(config.getBaseUrl() + config.getUrls().getClaimTypes(), ClaimType[].class);
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
            template.postForEntity(config.getBaseUrl() + config.getUrls().getPayment(), dto, Void.class);
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
            template.postForEntity(config.getBaseUrl() + config.getUrls().getNote(), dto, Void.class);
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
            template.postForEntity(config.getBaseUrl() + config.getUrls().getData(), data, Void.class);
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
            template.postForEntity(config.getBaseUrl() + config.getUrls().getState(), state, Void.class);
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
            template.postForEntity(config.getBaseUrl() + config.getUrls().getReserve(), reserve, Void.class);
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
            template.postForEntity(config.getBaseUrl() + config.getUrls().getType(), type, Void.class);
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
