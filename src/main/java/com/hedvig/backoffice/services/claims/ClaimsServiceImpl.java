package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.web.dto.ClaimDTO;
import com.hedvig.backoffice.web.dto.ClaimEventDTO;
import com.hedvig.backoffice.web.dto.ClaimTypeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

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

    @Override
    public List<ClaimDTO> list() throws ClaimException {
        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<ClaimDTO[]> response = template.getForEntity(baseUrl + claims, ClaimDTO[].class);
            return Arrays.asList(response.getBody());
        } catch (RestClientException e) {
            throw new ClaimsServiceException(e);
        }
    }

    @Override
    public ClaimDTO find(String id) throws ClaimException {
        RestTemplate template = new RestTemplate();
        try {
            ResponseEntity<ClaimDTO> response = template.getForEntity(baseUrl + claimById + "/" + id, ClaimDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new ClaimNotFoundException("claim with id " + id + " not found");
        } catch (RestClientException e) {
            throw new ClaimsServiceException(e);
        }
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

}
