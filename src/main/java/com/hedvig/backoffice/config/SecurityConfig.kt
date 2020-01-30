package com.hedvig.backoffice.config

import com.hedvig.backoffice.config.feign.ExternalServiceBadRequestException
import com.hedvig.backoffice.config.feign.ExternalServiceUnauthorizedException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity()
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig @Autowired constructor(
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
}

class GatekeeperFilter(
    private val gatekeeperClient: GatekeeperClient
) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        SecurityContextHolder.getContext().authentication = null
        if (request.session?.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY) != null) {
          request.session?.removeAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY)
        }

        val accessToken =
          request.cookies?.find { cookie ->
            cookie.name == "_hvg_at"
          }?.value

        accessToken?.apply {
          try {
            logger.info("fetch token info")
            val tokenInfo = gatekeeperClient.tokenInfo("Bearer $accessToken")
            SecurityContextHolder.getContext().authentication =
              GatekeeperAuthentication(tokenInfo, accessToken)
          } catch (e: ExternalServiceUnauthorizedException) {
            // we're not authenticated
          } catch (e: ExternalServiceBadRequestException) {
            // we're not authenticated
          }
        }

        chain.doFilter(request, response);
    }

  companion object {
    val logger = LoggerFactory.getLogger(GatekeeperFilter::class.java)
  }
}

@FeignClient(name = "gatekeeper", url = "\${gatekeeper.baseUrl}")
interface GatekeeperClient {
    @GetMapping("/oauth2/tokeninfo")
    fun tokenInfo(@RequestHeader("Authorization") authorization: String): TokenInfo
}

data class TokenInfo(
  val id: UUID,
  val subject: String,
  val scopes: Set<String>,
  val role: Role
)

enum class Role {
  NOBODY,
  IEX,
  IEX_EXTENDED,
  DEV,
  ROOT
}


class GatekeeperAuthentication(
  private val tokenInfo: TokenInfo,
  private val accessToken: String
) : AbstractAuthenticationToken(
  tokenInfo.scopes.map { scope -> SimpleGrantedAuthority(scope) }
) {
    override fun getPrincipal(): Any {
      return GatekeeperUser(tokenInfo.id, tokenInfo.subject, authorities)
    }

    override fun getCredentials(): Any {
        return accessToken
    }

  override fun isAuthenticated(): Boolean = true
}

class GatekeeperUser(
  val id: UUID,
  username: String,
  authorities: Collection<GrantedAuthority>
): User(username, "", authorities)
