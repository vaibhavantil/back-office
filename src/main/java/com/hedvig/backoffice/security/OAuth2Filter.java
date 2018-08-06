package com.hedvig.backoffice.security;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.filter.OAuth2AuthenticationFailureEvent;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetailsSource;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

public class OAuth2Filter extends OAuth2ClientAuthenticationProcessingFilter {

  private Set<String> hds;

  private ResourceServerTokenServices oauthTokenServices;

  private AuthenticationDetailsSource<HttpServletRequest, ?> detailsSource =
      new OAuth2AuthenticationDetailsSource();

  private ApplicationEventPublisher processingEventPublisher;

  public OAuth2Filter(String defaultFilterProcessesUrl, Set<String> hds) {
    super(defaultFilterProcessesUrl);
    this.hds = hds;
  }

  public void setOauthTokenServices(ResourceServerTokenServices oauthTokenServices) {
    this.oauthTokenServices = oauthTokenServices;
  }

  public void setDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> detailsSource) {
    this.detailsSource = detailsSource;
  }

  public void setProcessingEventPublisher(ApplicationEventPublisher processingEventPublisher) {
    this.processingEventPublisher = processingEventPublisher;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException, ServletException {

    OAuth2AccessToken accessToken;
    try {
      accessToken = restTemplate.getAccessToken();
    } catch (OAuth2Exception e) {
      BadCredentialsException bad = new BadCredentialsException("Could not obtain access token", e);
      publish(new OAuth2AuthenticationFailureEvent(bad));
      throw bad;
    }
    try {
      OAuth2Authentication result = oauthTokenServices.loadAuthentication(accessToken.getValue());
      if (detailsSource != null) {
        request.setAttribute(
            OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, accessToken.getValue());
        request.setAttribute(
            OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, accessToken.getTokenType());
        result.setDetails(detailsSource.buildDetails(request));
      }

      LinkedHashMap<String, String> details =
          (LinkedHashMap<String, String>) result.getUserAuthentication().getDetails();
      details.put("id_token", (String) accessToken.getAdditionalInformation().get("id_token"));
      OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
      if (refreshToken != null) {
        details.put("refresh_token", refreshToken.getValue());
      }

      if (hds.size() > 0 && !hds.contains(details.get("hd"))) {
        throw new BadCredentialsException("hd not allowed");
      }

      publish(new AuthenticationSuccessEvent(result));
      return result;
    } catch (InvalidTokenException e) {
      BadCredentialsException bad =
          new BadCredentialsException("Could not obtain user details from token", e);
      publish(new OAuth2AuthenticationFailureEvent(bad));
      throw bad;
    }
  }

  private void publish(ApplicationEvent event) {
    if (processingEventPublisher != null) {
      processingEventPublisher.publishEvent(event);
    }
  }
}
