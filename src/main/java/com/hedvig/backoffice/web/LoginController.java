package com.hedvig.backoffice.web;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.security.JWTDTO;
import com.hedvig.backoffice.security.JWTService;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.web.dto.PersonnelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class LoginController {

    private PersonnelService personnelService;
    private JWTService jwtService;

    @Autowired
    public LoginController(PersonnelService personnelService, JWTService jwtService) {
        this.personnelService = personnelService;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/login")
    public JWTDTO login(@RequestBody @Valid PersonnelDTO dto, HttpServletResponse response) throws AuthorizationException {
        Personnel personnel = personnelService.authorize(dto.getEmail(), dto.getPassword());
        JWTDTO jwt = jwtService.createTokenForUser(personnel.getEmail());
        jwtService.addTokenToHeader(jwt.getToken(), response);

        return jwt;
    }

}
