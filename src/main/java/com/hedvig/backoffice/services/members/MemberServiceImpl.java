package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.web.dto.MemberDTO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class MemberServiceImpl implements MemberService {

    private static Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    private String baseUrl;
    private String usersUrl;
    private String userByIdUrl;

    public MemberServiceImpl(@Value("${memberservice.baseUrl}") String baseUrl,
                             @Value("${memberservice.urls.users}") String usersUrl,
                             @Value("${memberservice.urls.userById}") String userByIdUrl) {
        this.baseUrl = baseUrl;
        this.usersUrl = usersUrl;
        this.userByIdUrl = userByIdUrl;

        logger.info("MEMBER SERVICE:");
        logger.info("class: " + MemberServiceImpl.class.getName());
        logger.info("base: " + baseUrl);
        logger.info("users: " + usersUrl);
        logger.info("id: " + userByIdUrl);
    }

    @HystrixCommand(fallbackMethod = "listFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION)
    @Override
    public List<MemberDTO> list() throws MemberServiceException {
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpEntity<String> request = new HttpEntity<>("test@email.com");
            ResponseEntity<MemberDTO[]> response = restTemplate.postForEntity(baseUrl + usersUrl, request, MemberDTO[].class);
            return Arrays.asList(response.getBody());
        } catch (RestClientException e) {
            throw new MemberServiceException(e);
        }
    }

    private List<MemberDTO> listFallback(Throwable e) {
        logger.error("failed members fetching", e);
        return new ArrayList<>();
    }

    @Override
    public List<MemberDTO> find(String query) throws MemberServiceException {
        List<MemberDTO> users = list();

        return users.stream()
                .filter(u -> u.getHid().contains(query) || (u.getFirstName() != null && u.getFirstName().contains(query)))
                .collect(Collectors.toList());
    }

    @Override
    public MemberDTO findByHid(String hid) throws MemberNotFoundException, MemberServiceException {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<MemberDTO> response = restTemplate.getForEntity(baseUrl + userByIdUrl + "/" + hid, MemberDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new MemberNotFoundException(hid);
        } catch (RestClientException e) {
            throw new MemberServiceException(e);
        }
    }

}
