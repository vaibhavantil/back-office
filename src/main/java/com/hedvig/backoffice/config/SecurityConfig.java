package com.hedvig.backoffice.config;

import com.google.common.collect.Sets;
import com.hedvig.backoffice.security.OAuth2Filter;
import com.hedvig.backoffice.security.OAuth2SuccessHandler;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private boolean oauthEnabled;
  private String[] hds;

  private OAuth2ClientContext clientContext;
  private PersonnelService personnelService;
  private AuthorizationCodeResourceDetails clientDetails;
  private ResourceServerProperties clientResource;

  @Autowired
  public SecurityConfig(
      OAuth2ClientContext clientContext,
      PersonnelService personnelService,
      @Value("${oauth.enabled:true}") boolean oauthEnabled,
      @Value("${oauth.hds}") String[] hds,
      AuthorizationCodeResourceDetails clientDetails,
      ResourceServerProperties clientResource) {

    this.clientContext = clientContext;
    this.personnelService = personnelService;

    this.oauthEnabled = oauthEnabled;
    this.hds = hds;

    this.clientDetails = clientDetails;
    this.clientResource = clientResource;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http =
        http.cors()
            .disable()
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
            .and()
            .sessionManagement()
            .maximumSessions(1)
            .and()
            .and()
            .logout()
            .logoutSuccessUrl("/login/oauth")
            .logoutUrl("/api/logout")
            .and()
            .addFilterBefore(
                ssoFilter(clientDetails, clientResource), BasicAuthenticationFilter.class);

    http = http.authorizeRequests().antMatchers("/").permitAll().and();

    if (oauthEnabled) {
      http.authorizeRequests()
          .antMatchers("/api/login/google")
          .permitAll()
          .antMatchers("/api/**")
          .authenticated()
          .antMatchers("/chat/**")
          .authenticated()
          .antMatchers("/graphiql")
          .authenticated();
    }
  }

  private Filter ssoFilter(
      AuthorizationCodeResourceDetails clientDetails, ResourceServerProperties clientResource) {
    OAuth2Filter filter = new OAuth2Filter("/api/login/google", Sets.newHashSet(hds));

    OAuth2RestTemplate template = new OAuth2RestTemplate(clientDetails, clientContext);
    filter.setRestTemplate(template);

    UserInfoTokenServices tokenServices =
        new UserInfoTokenServices(clientResource.getUserInfoUri(), clientDetails.getClientId());
    tokenServices.setRestTemplate(template);
    filter.setOauthTokenServices(tokenServices);

    filter.setAuthenticationSuccessHandler(successHandler());
    filter.setAuthenticationFailureHandler(
        new SimpleUrlAuthenticationFailureHandler("/api/logout"));

    return filter;
  }

  @Bean
  public OAuth2SuccessHandler successHandler() {
    return new OAuth2SuccessHandler(personnelService, "/login/process");
  }

  @Bean
  public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(filter);
    registration.setOrder(-100);
    return registration;
  }
}
