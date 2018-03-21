package com.hedvig.backoffice.security;

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
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetailsSource;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;

public class OAuth2Filter extends OAuth2ClientAuthenticationProcessingFilter {

    public OAuth2Filter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    private ResourceServerTokenServices oauthTokenServices;

    private AuthenticationDetailsSource<HttpServletRequest, ?> detailsSource = new OAuth2AuthenticationDetailsSource();

    private ApplicationEventPublisher processingEventPublisher;

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
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
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
            if (detailsSource !=null) {
                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, accessToken.getValue());
                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, accessToken.getTokenType());
                result.setDetails(detailsSource.buildDetails(request));
            }

            LinkedHashMap<String, String> details
                    = (LinkedHashMap<String, String>) result.getUserAuthentication().getDetails();
            details.put("id_token", (String) accessToken.getAdditionalInformation().get("id_token"));

            publish(new AuthenticationSuccessEvent(result));
            return result;
        }
        catch (InvalidTokenException e) {
            BadCredentialsException bad = new BadCredentialsException("Could not obtain user details from token", e);
            publish(new OAuth2AuthenticationFailureEvent(bad));
            throw bad;
        }
    }

    private void publish(ApplicationEvent event) {
        if (processingEventPublisher !=null) {
            processingEventPublisher.publishEvent(event);
        }
    }
}