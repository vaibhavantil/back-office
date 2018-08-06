package com.hedvig.backoffice.security;

import com.hedvig.backoffice.services.personnel.PersonnelService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final PersonnelService personnelService;

  public OAuth2SuccessHandler(PersonnelService personnelService, String targetUrl) {
    this.personnelService = personnelService;
    setDefaultTargetUrl(targetUrl);
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {

    logger.info("user successful logged in, path: " + request.getRequestURL());
    personnelService.processAuthorization(authentication);
    handle(request, response, authentication);
    clearAuthenticationAttributes(request);
  }
}
