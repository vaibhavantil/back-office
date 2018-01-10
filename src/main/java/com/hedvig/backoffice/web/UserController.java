package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.users.UserNotFoundException;
import com.hedvig.backoffice.services.users.UserService;
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
    public List<UserDTO> users() {
        return userService.list();
    }

    @GetMapping("/search/{search}")
    public UserDTO find(@PathVariable String search) throws UserNotFoundException {
        return userService.find(search);
    }

}
