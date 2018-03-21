package com.hedvig.backoffice.services.personnel;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.repository.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;

@Service
public class PersonnelServiceImpl implements PersonnelService {

    private final PersonnelRepository personnelRepository;

    @Autowired
    public PersonnelServiceImpl(PersonnelRepository personnelRepository) {
        this.personnelRepository = personnelRepository;
    }

    @Transactional
    @Override
    public void processAuthorization(Authentication auth) {
        OAuth2Authentication authentication = (OAuth2Authentication) auth;

        LinkedHashMap<String, String> details
                = (LinkedHashMap<String, String>) authentication.getUserAuthentication().getDetails();

        String id = details.get("id");
        Personnel personnel = personnelRepository.findById(id).orElseGet(() -> new Personnel(id));
        personnel.setEmail(details.get("email"));
        personnel.setName(details.get("name"));
        personnel.setPicture(details.get("picture"));
        personnel.setIdToken(details.get("id_token"));

        personnelRepository.save(personnel);
    }
}
