package com.hedvig.backoffice.services.users;

import com.hedvig.backoffice.web.dto.UserDTO;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserServiceStub implements UserService {

    private List<UserDTO> users;

    public UserServiceStub() {
        users = IntStream.range(0, 10).mapToObj(i -> {
            String id = Long.toString(RandomUtils.nextLong());
            String name = "Test user " + i;

            return new UserDTO(id, name);
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> list() {
        return users;
    }

    @Override
    public UserDTO find(String query) throws UserNotFoundException {
        return users.stream()
                .filter(u -> u.getHid().contains(query) || u.getName().contains(query))
                .findAny()
                .orElse(new UserDTO(query, query));
    }

    @Override
    public UserDTO findByHid(String hid) throws UserNotFoundException {
        return find(hid);
    }

}
