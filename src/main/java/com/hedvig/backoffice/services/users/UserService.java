package com.hedvig.backoffice.services.users;

import com.hedvig.backoffice.web.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> loadUserList();
    UserDTO findUser(String query) throws UserNotFoundException;

}
