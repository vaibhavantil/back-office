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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

    @HystrixCommand(fallbackMethod = "listFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION)
    @Override
    public List<MemberDTO> list() throws MemberServiceException {
        return search("", "");
    }

    private List<MemberDTO> listFallback(Throwable e) {
        logger.error("failed members fetching", e);
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "searchFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION)
    @Override
    public List<MemberDTO> search(String status, String query) throws MemberServiceException {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<MemberDTO[]> response
                    = restTemplate.getForEntity(baseUrl + searchUrl + "?status=" + status + "&query=" + query, MemberDTO[].class);
            return Arrays.asList(response.getBody());
        } catch (RestClientException e) {
            throw new MemberServiceException(e);
        }
    }

    private List<MemberDTO> searchFallback(String status, String query, Throwable e) {
        logger.error("failed members fetching", e);
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "findByHidFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION,
            ignoreExceptions = { MemberNotFoundException.class })
    @Override
    public MemberDTO findByHid(String hid) throws MemberNotFoundException, MemberServiceException {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<MemberDTO> response = restTemplate.getForEntity(baseUrl + getByIdUrl + "/" + hid, MemberDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new MemberNotFoundException(hid);
        } catch (RestClientException e) {
            throw new MemberServiceException(e);
        }
    }

    private MemberDTO findByHidFallback(String hid, Throwable e) throws MemberServiceException {
        logger.error("failed member fetching", e);
        throw new MemberServiceException(e);
    }

}
