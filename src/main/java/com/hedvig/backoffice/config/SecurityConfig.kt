package com.hedvig.backoffice.config

import com.hedvig.backoffice.security.GatekeeperFilter
import com.hedvig.backoffice.services.gatekeeper.GatekeeperService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity()
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig @Autowired constructor(
    private val gatekeeperService: GatekeeperService
) : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .cors().disable()
            .csrf().disable()
            .headers()
            .frameOptions()
            .disable()
            .and()
            .addFilterBefore(GatekeeperFilter(gatekeeperService), BasicAuthenticationFilter::class.java)
            .authorizeRequests().antMatchers("/").permitAll().and()
            .authorizeRequests().antMatchers("/api/**").authenticated()
            .antMatchers("/chat/**").authenticated()
            .antMatchers("/graphiql").authenticated();
    }
}
