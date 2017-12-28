package com.hedvig.backoffice.services.users;

import com.hedvig.backoffice.web.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserServiceStub implements UserService {

    private List<UserDTO> users;

    public UserServiceStub() {
        users = IntStream.range(0, 10).mapToObj(i -> {
            String id = UUID.randomUUID().toString();
            String name = "Test user " + i;

            return new UserDTO(id, name);
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> loadUserList() {
        return users;
    }

    @Override
    public UserDTO findUser(String query) throws UserNotFoundException {
        return users.stream()
                .filter(u -> u.getId().contains(query) || u.getName().contains(query)).
                findAny()
                .orElseThrow(() -> new UserNotFoundException(query));
    }


}
