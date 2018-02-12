package com.hedvig.backoffice.services.users;

import com.hedvig.backoffice.web.dto.UserDTO;
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


public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private String baseUrl;
    private String usersUrl;
    private String userByIdUrl;

    public UserServiceImpl(@Value("${userservice.baseUrl}") String baseUrl,
                           @Value("${userservice.urls.users}") String usersUrl,
                           @Value("${userservice.urls.userById}") String userByIdUrl) {
        this.baseUrl = baseUrl;
        this.usersUrl = usersUrl;
        this.userByIdUrl = userByIdUrl;

        logger.info("USER SERVICE:");
        logger.info("class: " + UserServiceImpl.class.getName());
        logger.info("base: " + baseUrl);
        logger.info("users: " + usersUrl);
        logger.info("id: " + userByIdUrl);
    }

    @HystrixCommand(fallbackMethod = "listFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    }, raiseHystrixExceptions = HystrixException.RUNTIME_EXCEPTION)
    @Override
    public List<UserDTO> list() throws UserServiceException {
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpEntity<String> request = new HttpEntity<>("test@email.com");
            ResponseEntity<UserDTO[]> response = restTemplate.postForEntity(baseUrl + usersUrl, request, UserDTO[].class);
            return Arrays.asList(response.getBody());
        } catch (RestClientException e) {
            throw new UserServiceException(e);
        }
    }

    private List<UserDTO> listFallback(Throwable e) {
        logger.error("failed user fetching", e);
        return new ArrayList<>();
    }

    @Override
    public List<UserDTO> find(String query) throws UserServiceException {
        List<UserDTO> users = list();

        return users.stream()
                .filter(u -> u.getHid().contains(query) || (u.getFirstName() != null && u.getFirstName().contains(query)))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findByHid(String hid) throws UserNotFoundException, UserServiceException {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<UserDTO> response = restTemplate.getForEntity(baseUrl + userByIdUrl + "/" + hid, UserDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new UserNotFoundException(hid);
        } catch (RestClientException e) {
            throw new UserServiceException(e);
        }
    }

}
