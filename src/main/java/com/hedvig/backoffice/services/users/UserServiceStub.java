package com.hedvig.backoffice.services.users;

import com.hedvig.backoffice.web.dto.UserDTO;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserServiceStub implements UserService {

    private List<UserDTO> users;

    public UserServiceStub() {
        users = IntStream.range(0, 10).mapToObj(i -> {
            String name = "Test user " + i;

            return new UserDTO(RandomUtils.nextLong(), name);
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> list() throws UserServiceException {
        return users;
    }

    @Override
    public List<UserDTO> find(String query) throws UserNotFoundException, UserServiceException {
        List<UserDTO> result = users.stream()
                .filter(u -> u.getHid().contains(query) || u.getFirstName().contains(query))
                .collect(Collectors.toList());

        if (result.size() == 0) {
            result.add(new UserDTO(RandomUtils.nextLong(), query));
        }

        return result;
    }

    @Override
    public UserDTO findByHid(String hid) throws UserNotFoundException, UserServiceException {
        return users.stream()
                .filter(u -> u.getHid().equals(hid))
                .findAny()
                .orElse(new UserDTO(Long.parseLong(hid), ""));
    }

}
