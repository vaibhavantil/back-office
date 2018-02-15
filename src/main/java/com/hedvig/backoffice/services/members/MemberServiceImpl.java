package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.web.dto.MemberDTO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class MemberServiceImpl implements MemberService {

    private static Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    private String baseUrl;
    private String searchUrl;
    private String getByIdUrl;

    public MemberServiceImpl(@Value("${memberservice.baseUrl}") String baseUrl,
                             @Value("${memberservice.urls.search}") String searchUrl,
                             @Value("${memberservice.urls.getById}") String getByIdUrl) {
        this.baseUrl = baseUrl;
        this.searchUrl = searchUrl;
        this.getByIdUrl = getByIdUrl;

        logger.info("MEMBER SERVICE:");
        logger.info("class: " + MemberServiceImpl.class.getName());
        logger.info("base: " + baseUrl);
        logger.info("users: " + searchUrl);
        logger.info("id: " + getByIdUrl);
    }

    @HystrixCommand(fallbackMethod = "searchFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION)
    @Override
    public Optional<List<MemberDTO>> search(String status, String query) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MemberDTO[]> response
                = restTemplate.getForEntity(baseUrl + searchUrl + "?status=" + status + "&query=" + query, MemberDTO[].class);
        return Optional.of(Arrays.asList(response.getBody()));
    }

    private Optional<List<MemberDTO>> searchFallback(String status, String query, Throwable e) {
        logger.error("failed members fetching", e);
        return Optional.empty();
    }

    @HystrixCommand(fallbackMethod = "findByHidFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION,
            ignoreExceptions = { MemberNotFoundException.class })
    @Override
    public Optional<MemberDTO> findByHid(String hid) throws MemberNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<MemberDTO> response = restTemplate.getForEntity(baseUrl + getByIdUrl + "/" + hid, MemberDTO.class);
            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException e) {
            throw new MemberNotFoundException(hid);
        }
    }

    private Optional<MemberDTO> findByHidFallback(String hid, Throwable e) {
        logger.error("failed member fetching", e);
        return Optional.empty();
    }

}
