package com.hedvig.backoffice.services.users;

import com.hedvig.backoffice.web.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> list() throws UserServiceException;
    List<UserDTO> find(String query) throws UserNotFoundException, UserServiceException;
    UserDTO findByHid(String hid) throws UserNotFoundException, UserServiceException;

}
