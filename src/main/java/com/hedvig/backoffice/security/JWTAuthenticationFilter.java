package com.hedvig.backoffice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Collections.emptyList;

public class JWTAuthenticationFilter extends GenericFilterBean {

    private JWTService jwtService;

    @Autowired
    public JWTAuthenticationFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        String user = null;
        try {
            user = jwtService.resolveUserFromToken((HttpServletRequest) request);
        } catch (JWTInvalidTokenException e) {
            HttpServletResponse res = (HttpServletResponse) response;
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        if (user != null) {
            UsernamePasswordAuthenticationToken authentication
                    = new UsernamePasswordAuthenticationToken(user, null, emptyList());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
