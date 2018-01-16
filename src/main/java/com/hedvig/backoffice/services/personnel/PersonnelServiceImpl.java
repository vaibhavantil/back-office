package com.hedvig.backoffice.services.personnel;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.DatabaseLoader;
import com.hedvig.backoffice.web.dto.PersonnelDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonnelServiceImpl implements PersonnelService {

    private final DatabaseLoader databaseLoader;
    private final PasswordEncoder passwordEncoder;
    private final PersonnelRepository personnelRepository;

    @Autowired
    public PersonnelServiceImpl(DatabaseLoader databaseLoader, PasswordEncoder passwordEncoder,
                                PersonnelRepository personnelRepository) {
        this.databaseLoader = databaseLoader;
        this.passwordEncoder = passwordEncoder;
        this.personnelRepository = personnelRepository;
    }

    @PostConstruct
    public void setup() {
        databaseLoader.createAdmin();
    }

    @Override
    public Personnel authorize(String email, String password) throws AuthorizationException {
        Optional<Personnel> personnelOptional = personnelRepository.findByEmail(email);

        if (!personnelOptional.isPresent()) {
            throw new AuthorizationException(String.format("personnel %s not found", email));
        }

        Personnel personnel = personnelOptional.get();
        if (passwordEncoder.matches(password, personnel.getPassword())) {
            return personnel;
        } else {
            throw new AuthorizationException("incorrect password");
        }
    }

    @Override
    @Transactional
    public void register(PersonnelDTO dto) throws PersonnelServiceException {
        if (StringUtils.isBlank(dto.getPassword())) {
            throw new PersonnelServiceException("password required");
        }

        Optional<Personnel> optional = personnelRepository.findByEmail(dto.getEmail());
        if (!optional.isPresent()) {
            Personnel personnel = PersonnelDTO.toDomain(dto);
            personnel.setPassword(passwordEncoder.encode(dto.getPassword()));
            personnelRepository.save(personnel);
        } else {
            throw new PersonnelServiceException(String.format("User with email %s already exists", dto.getEmail()));
        }
    }

    @Override
    @Transactional
    public void remove(long id) throws PersonnelServiceException {
        Optional<Personnel> optional = personnelRepository.findById(id);
        if (optional.isPresent()) {
            personnelRepository.delete(optional.get());
        } else {
            throw new PersonnelServiceException(String.format("User with id %s not found", id));
        }
    }

    @Override
    public void update(long id, PersonnelDTO dto) throws PersonnelServiceException {
        Optional<Personnel> optional = personnelRepository.findById(id);
        if (optional.isPresent()) {
            Personnel p = optional.get();

            if (!p.getEmail().equals(dto.getEmail())) {
                p.setEmail(dto.getEmail());
            }

            if (StringUtils.isNotBlank(dto.getPassword())) {
                p.setPassword(passwordEncoder.encode(dto.getPassword()));
            }

            personnelRepository.save(p);
        } else {
            throw new PersonnelServiceException(String.format("User with id %s not found", id));
        }
    }

    @Override
    @Transactional
    public List<PersonnelDTO> list() {
        return personnelRepository
                .all()
                .map(PersonnelDTO::fromDomain)
                .collect(Collectors.toList());
    }

}
