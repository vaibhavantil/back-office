package com.hedvig.backoffice.services;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.repository.PersonnelRepository;
import lombok.val;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Prepare test data, this is only needed in development.
 */

@Component
public class DatabaseLoader {
    private final PersonnelRepository personnelRepository;

    private final PasswordEncoder passwordEncoder;

    public DatabaseLoader(PersonnelRepository personnelRepository, PasswordEncoder passwordEncoder) {
        this.personnelRepository = personnelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser() {
        val email = "victor@hedvig.com";
        if (!personnelRepository.findByEmail(email).isPresent()) {
            personnelRepository.save(new Personnel(email, passwordEncoder.encode("123")));
        }
    }
}
