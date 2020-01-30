package com.hedvig.backoffice.services.personnel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.config.GatekeeperUser;
import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.web.dto.PersonnelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;


@Service
public class PersonnelServiceImpl implements PersonnelService {
  private final PersonnelRepository personnelRepository;

  @Autowired
  public PersonnelServiceImpl(PersonnelRepository personnelRepository) {
    this.personnelRepository = personnelRepository;
  }

  @Transactional
  @Override
  public Personnel storeAuthentication(Principal auth) {
    if (!(auth instanceof Authentication)) {
      throw new IllegalArgumentException("Expected principal to store to be Authentication but was " + auth.getClass());
    }

    final GatekeeperUser details = (GatekeeperUser) ((Authentication) auth).getPrincipal();

    Personnel personnel = personnelRepository.findByEmail(details.getUsername())
      .orElseGet(() -> new Personnel(details.getId().toString()));
    personnel.setEmail(details.getUsername());

    personnelRepository.save(personnel);

    return personnel;
  }

  @Override
  public PersonnelDTO me(String email) throws AuthorizationException {
    return PersonnelDTO.fromDomain(
      personnelRepository.findByEmail(email)
        .orElseThrow(AuthorizationException::new)
    );
  }

  @Override
  public Personnel getPersonnelByEmail(String email) throws AuthorizationException {
    return personnelRepository.findByEmail(email).orElseThrow(AuthorizationException::new);
  }

  @Transactional
  @Override
  public String getIdToken(String id) {
    return "deprecated";
  }
}
