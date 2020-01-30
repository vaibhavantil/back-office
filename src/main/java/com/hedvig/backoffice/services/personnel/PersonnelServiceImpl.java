package com.hedvig.backoffice.services.personnel;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.web.dto.PersonnelDTO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PersonnelServiceImpl implements PersonnelService {
  private final PersonnelRepository personnelRepository;
  private final ObjectMapper mapper;

  @Autowired
  public PersonnelServiceImpl(
      PersonnelRepository personnelRepository,
      ObjectMapper mapper
  ) {
    this.personnelRepository = personnelRepository;
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

  @Override
  @Deprecated
  public String getIdToken(Personnel personnel) {
    return "deprecated";
  }
}
