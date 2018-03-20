package com.hedvig.backoffice.security;

import com.hedvig.backoffice.services.personnel.PersonnelService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final PersonnelService personnelService;

    public OAuth2SuccessHandler(PersonnelService personnelService, String targetUrl) {
        this.personnelService = personnelService;
        setDefaultTargetUrl(targetUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        personnelService.processAuthorization(authentication);
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }
}
