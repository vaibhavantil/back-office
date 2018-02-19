package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.claims.data.Claim;
import com.hedvig.backoffice.web.dto.claims.*;
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

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClaimsServiceImpl implements ClaimsService {

    private static Logger logger = LoggerFactory.getLogger(ClaimsServiceImpl.class);

    private final String baseUrl;
    private final String claims;
    private final String claimById;

    @Autowired
    public ClaimsServiceImpl(@Value("${claims.baseUrl}") String baseUrl,
                             @Value("${claims.urls.claims}") String claims,
                             @Value("${claims.urls.claimById}") String claimById) {

        this.baseUrl = baseUrl;
        this.claims = claims;
        this.claimById = claimById;

        logger.info("CLAIMS SERVICE:");
        logger.info("class: " + ClaimsServiceImpl.class.getName());
        logger.info("base: " + baseUrl);
        logger.info("claims: " + claims);
        logger.info("id: " + claimById);
    }

    @HystrixCommand(fallbackMethod = "listFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION)
    @Override
    public List<ClaimDTO> list() {
        RestTemplate template = new RestTemplate();
        ResponseEntity<Claim[]> response = template.getForEntity(baseUrl + claims, Claim[].class);
        return Arrays.stream(response.getBody())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ClaimDTO> listFallback(Throwable e) {
        logger.error("failed claims fetching", e);
        return null;
    }

    private ClaimDTO toDTO(Claim claim) {
        return new ClaimDTO(
                claim.getId(),
                claim.getUserId(),
                claim.getState(),
                null,
                null,
                claim.getAudioURL(),
                null,
                null,
                claim.getRegistrationDate().toInstant(ZoneOffset.UTC));
    }

    @HystrixCommand(fallbackMethod = "findFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION,
            ignoreExceptions = { ClaimNotFoundException.class })
    @Override
    public ClaimDTO find(String id) throws ClaimException {
        RestTemplate template = new RestTemplate();
        try {
            ResponseEntity<Claim> response = template.getForEntity(baseUrl + claimById + "?claimID=" + id, Claim.class);
            return toDTO(response.getBody());
        } catch (HttpClientErrorException e) {
            throw new ClaimNotFoundException("claim with id " + id + " not found");
        }
    }

    private ClaimDTO findFallback(String id, Throwable t) {
        logger.error("failed claim fetching", t);
        return null;
    }

    @Override
    public List<ClaimTypeDTO> types() throws ClaimException {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public void save(ClaimDTO dto) throws ClaimException {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public List<ClaimEventDTO> events(String id) throws ClaimException {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public void addEvent(ClaimEventDTO dto) throws ClaimException {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public List<ClaimPayoutDTO> payouts(String id) throws ClaimException {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public ClaimPayoutDTO addPayout(ClaimPayoutDTO dto) throws ClaimException {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public void updatePayout(ClaimPayoutDTO dto) throws ClaimException {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public void removePayout(String id, String claimId) throws ClaimException {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public List<ClaimNoteDTO> notes(String id) throws ClaimException {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public ClaimNoteDTO addNote(ClaimNoteDTO dto) throws ClaimException {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public void removeNote(String id, String claimId) throws ClaimException {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public void changeType(String id, String type) throws ClaimException {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public void changeStatus(String id, ClaimStatus status) throws ClaimException {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public void setResume(String id, BigDecimal resume) throws ClaimException {
        throw new RuntimeException("Not implemented yet!");
    }

    @Override
    public void addDetails(String id, ClaimDetailsDTO dto) throws ClaimException {
        throw new RuntimeException("Not implemented yet!");
    }

}
