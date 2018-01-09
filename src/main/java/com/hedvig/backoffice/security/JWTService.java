package com.hedvig.backoffice.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JWTService {

    JWTDTO createTokenForUser(String email);
    void addTokenToHeader(String jwtToken, HttpServletResponse response);
    String resolveUserFromToken(HttpServletRequest request) throws JWTInvalidTokenException;

}
