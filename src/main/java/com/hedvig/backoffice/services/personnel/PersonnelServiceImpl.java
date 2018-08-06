package com.hedvig.backoffice.services.personnel;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.web.dto.PersonnelDTO;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonnelServiceImpl implements PersonnelService {

  private final PersonnelRepository personnelRepository;
  private final AuthorizationCodeResourceDetails clientDetails;
  private final ObjectMapper mapper;

  @Autowired
  public PersonnelServiceImpl(
      PersonnelRepository personnelRepository,
      AuthorizationCodeResourceDetails clientDetails,
      ObjectMapper mapper) {
    this.personnelRepository = personnelRepository;
    this.clientDetails = clientDetails;
    this.mapper = mapper;
  }

  @Transactional
  @Override
  public void processAuthorization(Authentication auth) {
    OAuth2Authentication authentication = (OAuth2Authentication) auth;

    LinkedHashMap<String, String> details =
        (LinkedHashMap<String, String>) authentication.getUserAuthentication().getDetails();

    String id = details.get("id");
    Personnel personnel = personnelRepository.findById(id).orElseGet(() -> new Personnel(id));
    personnel.setEmail(details.get("email"));
    personnel.setName(details.get("name"));
    personnel.setPicture(details.get("picture"));
    personnel.setIdToken(details.get("id_token"));
    personnel.setRefreshToken(details.get("refresh_token"));

    personnelRepository.save(personnel);
  }

  @Transactional
  @Override
  public List<PersonnelDTO> list() {
    return personnelRepository.all().map(PersonnelDTO::fromDomain).collect(Collectors.toList());
  }

  @Override
  public PersonnelDTO me(String id) throws AuthorizationException {
    return PersonnelDTO.fromDomain(
        personnelRepository.findById(id).orElseThrow(AuthorizationException::new));
  }

  @Override
  public Personnel getPersonnel(String id) throws AuthorizationException {
    return personnelRepository.findById(id).orElseThrow(AuthorizationException::new);
  }

  @Transactional
  @Override
  public String getIdToken(String id) {
    Personnel personnel = personnelRepository.findById(id).orElseThrow(TokenRefreshException::new);
    return getIdToken(personnel);
  }

  @Transactional
  @Override
  public String getIdToken(Personnel personnel) {
    if (personnel.getIdToken() == null) {
      throw new TokenRefreshException();
    }

    DecodedJWT jwt = JWT.decode(personnel.getIdToken());

    if (new Date().after(jwt.getExpiresAt())) {
      JsonNode result = refreshToken(personnel);
      String idToken = result.get("id_token").asText();
      personnel.setIdToken(idToken);
      personnelRepository.save(personnel);
      return idToken;
    }

    return personnel.getIdToken();
  }

  private JsonNode refreshToken(Personnel personnel) {
    OkHttpClient client = new OkHttpClient();
    String buff =
        "client_id="
            + clientDetails.getClientId()
            + "&client_secret="
            + clientDetails.getClientSecret()
            + "&grant_type=refresh_token"
            + "&refresh_token="
            + personnel.getRefreshToken();

    RequestBody body =
        RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), buff);
    Request request =
        new Request.Builder().url("https://www.googleapis.com/oauth2/v4/token").post(body).build();

    try {
      Response response = client.newCall(request).execute();
      String responseBody = response.body().string();
      return mapper.readTree(responseBody);
    } catch (Exception e) {
      throw new TokenRefreshException(e);
    }
  }
}
