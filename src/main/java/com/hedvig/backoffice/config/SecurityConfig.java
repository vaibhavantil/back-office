package com.hedvig.backoffice.config;

import com.google.common.collect.Sets;
import com.hedvig.backoffice.security.OAuth2Filter;
import com.hedvig.backoffice.security.OAuth2SuccessHandler;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.Filter;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private boolean oauthEnabled;
    private boolean enableHttps;
    private String[] corsOrigins;
    private String[] corsMethods;
    private String[] hds;
    private String successfulRedirectUrl;
    private String failureRedirectUrl;

    private OAuth2ClientContext clientContext;
    private PersonnelService personnelService;

    @Autowired
    public SecurityConfig(OAuth2ClientContext clientContext,
                          PersonnelService personnelService,
                          @Value("${oauth.enabled:true}") boolean oauthEnabled,
                          @Value("${oauth.enableHttps:true}") boolean enableHttps,
                          @Value("${oauth.successfulRedirectUrl}") String successfulRedirectUrl,
                          @Value("${oauth.failureRedirectUrl}") String failureRedirectUrl,
                          @Value("${oauth.hds}") String[] hds,
                          @Value("${cors.origins}") String[] corsOrigins,
                          @Value("${cors.methods}") String[] corsMethods) {

        this.clientContext = clientContext;
        this.personnelService = personnelService;

        this.successfulRedirectUrl = successfulRedirectUrl;
        this.failureRedirectUrl = failureRedirectUrl;
        this.oauthEnabled = oauthEnabled;
        this.enableHttps = enableHttps;
        this.hds = hds;
        this.corsOrigins = corsOrigins;
        this.corsMethods = corsMethods;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http = http
                .cors().and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and().sessionManagement().maximumSessions(1).and()
                .and().logout().logoutSuccessUrl("/login/oauth").logoutUrl("/api/logout")
                .and()
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);

        http = http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .and();

        if (oauthEnabled) {
            http.authorizeRequests()
                    .antMatchers("/api/login/google").permitAll()
                    .antMatchers("/api/**").authenticated()
                    .antMatchers("/chat/**").authenticated();
        }

        if (enableHttps) {
            http.requiresChannel()
                    .antMatchers("/login/process").requiresSecure()
                    .antMatchers("/api/login/google").requiresSecure();
        }
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(corsOrigins));
        configuration.setAllowedMethods(Arrays.asList(corsMethods));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    private Filter ssoFilter() {
        OAuth2Filter filter = new OAuth2Filter("/api/login/google", Sets.newHashSet(hds));

        OAuth2RestTemplate template = new OAuth2RestTemplate(google(), clientContext);
        filter.setRestTemplate(template);

        UserInfoTokenServices tokenServices = new UserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId());
        tokenServices.setRestTemplate(template);
        filter.setOauthTokenServices(tokenServices);

        filter.setAuthenticationSuccessHandler(successHandler());
        filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(failureRedirectUrl));

        return filter;
    }

    @Bean
    public OAuth2SuccessHandler successHandler() {
        return new OAuth2SuccessHandler(personnelService, successfulRedirectUrl);
    }

    @Bean
    @ConfigurationProperties("oauth.google.client")
    public AuthorizationCodeResourceDetails google() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("oauth.google.resource")
    public ResourceServerProperties googleResource() {
        return new ResourceServerProperties();
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }
}
