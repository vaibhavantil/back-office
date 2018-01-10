package com.hedvig.backoffice.services.users;

import com.hedvig.backoffice.web.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> list();
    UserDTO find(String query) throws UserNotFoundException;
    UserDTO findByHid(String hid) throws UserNotFoundException;

}
