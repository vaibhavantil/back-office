package com.hedvig.backoffice.services.login;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.repository.UserRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.DatabaseLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class PersonnelServiceImpl implements PersonnelService {

    private final DatabaseLoader databaseLoader;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public PersonnelServiceImpl(DatabaseLoader databaseLoader, PasswordEncoder passwordEncoder,
                                UserRepository userRepository) {
        this.databaseLoader = databaseLoader;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void setup() {
        databaseLoader.createUser();
    }

    @Override
    public Personnel authorizeUser(String email, String password) throws AuthorizationException {
        Optional<Personnel> userOptional = userRepository.findByEmail(email);

        if (!userOptional.isPresent()) {
            throw new AuthorizationException(String.format("personnel %s not found", email));
        }

        Personnel personnel = userOptional.get();
        if (passwordEncoder.matches(password, personnel.getPassword())) {
            return personnel;
        } else {
            throw new AuthorizationException("incorrect password");
        }
    }

}
