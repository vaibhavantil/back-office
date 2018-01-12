package com.hedvig.backoffice.services.users;

import com.hedvig.backoffice.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


public class UserServiceImpl implements UserService {

    private String baseUrl;
    private String usersUrl;
    private String userByIdUrl;

    public UserServiceImpl(@Value("${userservice.baseUrl}") String baseUrl,
                           @Value("${userservice.urls.users}") String usersUrl,
                           @Value("${userservice.urls.userById}") String userByIdUrl) {
        this.baseUrl = baseUrl;
        this.usersUrl = usersUrl;
        this.userByIdUrl = userByIdUrl;
    }

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

    @Override
    public List<UserDTO> find(String query) throws UserNotFoundException, UserServiceException {
        return null;
    }

    @Override
    public UserDTO findByHid(String hid) throws UserNotFoundException, UserServiceException {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<UserDTO> response = restTemplate.getForEntity(baseUrl + userByIdUrl + "/" + hid, UserDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                throw new UserNotFoundException(hid);
            }
            throw new UserServiceException(e);
        } catch (RestClientException e) {
            throw new UserServiceException(e);
        }
    }

}
