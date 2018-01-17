package com.hedvig.backoffice.config;

import com.hedvig.backoffice.security.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private boolean jwtEnabled;
    private String[] corsOrigins;
    private String[] corsMethods;

    private ApplicationContext context;

    @Autowired
    public SecurityConfiguration(ApplicationContext context,
                                 @Value("${jwt.enabled:true}") boolean jwtEnabled,
                                 @Value("${cors.origins}") String[] corsOrigins,
                                 @Value("${cors.methods}") String[] corsMethods) {
        this.context = context;

        this.jwtEnabled = jwtEnabled;
        this.corsOrigins = corsOrigins;
        this.corsMethods = corsMethods;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http = http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().disable()
                .and()
                .addFilterBefore(context.getAutowireCapableBeanFactory()
                        .createBean(JWTAuthenticationFilter.class), UsernamePasswordAuthenticationFilter.class);

        http = http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .and();

        if (jwtEnabled) {
            http.authorizeRequests()
                    .antMatchers("/api/login").permitAll()
                    .antMatchers("/api/**").authenticated()
                    .antMatchers("/chat/**").authenticated();
        }
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(corsOrigins));
        configuration.setAllowedMethods(Arrays.asList(corsMethods));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
