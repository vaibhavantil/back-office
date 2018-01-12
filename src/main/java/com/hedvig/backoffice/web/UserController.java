package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.users.UserNotFoundException;
import com.hedvig.backoffice.services.users.UserService;
import com.hedvig.backoffice.services.users.UserServiceException;
import com.hedvig.backoffice.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> users() throws UserServiceException {
        return userService.list();
    }

    @GetMapping("/{hid}")
    public UserDTO findOne(@PathVariable String hid) throws UserServiceException, UserNotFoundException {
        return userService.findByHid(hid);
    }

    @GetMapping("/search/{search}")
    public List<UserDTO> find(@PathVariable String search) throws UserNotFoundException, UserServiceException {
        return userService.find(search);
    }

}
