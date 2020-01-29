package com.hedvig.backoffice.config

import com.hedvig.backoffice.config.feign.ExternalServiceUnauthorizedException
import com.hedvig.backoffice.security.OAuth2SuccessHandler
import com.hedvig.backoffice.services.personnel.PersonnelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.handler.annotation.Header
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableOAuth2Client
@EnableOAuth2Sso
class SecurityConfig @Autowired constructor(
    private val personnelService: PersonnelService,
    private val gatekeeperClient: GatekeeperClient
) : WebSecurityConfigurerAdapter() {




    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
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
                GatekeeperFilter(gatekeeperClient), BasicAuthenticationFilter::class.java)
            .authorizeRequests().antMatchers("/").permitAll().and()
            .authorizeRequests()
            .antMatchers("/api/login/google")
            .permitAll()
            .antMatchers("/api/**")
            .authenticated()
            .antMatchers("/chat/**")
            .authenticated()
            .antMatchers("/graphiql")
            .authenticated();
    }

    @Bean
    fun successHandler(): OAuth2SuccessHandler {
        return OAuth2SuccessHandler(personnelService, "/login/process")
    }

}

class GatekeeperFilter(
    private val gatekeeperClient: GatekeeperClient
) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {

        val accessToken = if (request is HttpServletRequest) {
            request.cookies.find { cookie ->
                cookie.name == "_hvg_at"
            }?.value
        } else null

        accessToken?.let {
            val tokenInfo = try {
                gatekeeperClient.tokenInfo("Bearer $accessToken")
            } catch (feign: ExternalServiceUnauthorizedException){
                return
            }
            SecurityContextHolder.getContext().authentication = GatekeeperAuthentication(tokenInfo)
        }

        chain.doFilter(request, response);
    }
}

@FeignClient(name = "gatekeeper", url = "\${gatekeeper.baseUrl}")
interface GatekeeperClient {
    @GetMapping("/oauth2/tokeninfo")
    fun tokenInfo(@Header(value = "authorization") authorization: String): TokenInfo
}

data class TokenInfo(
    val username: String,
    val scopes: Set<String>
)

class GatekeeperAuthentication(private val tokenInfo: TokenInfo) : AbstractAuthenticationToken(tokenInfo.scopes.map { scope -> SimpleGrantedAuthority(scope) }) {

    override fun getPrincipal(): Any {
        return tokenInfo.username
    }

    override fun getCredentials(): Any {
        return tokenInfo.username
    }
}
