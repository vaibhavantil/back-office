package com.hedvig.backoffice.security

import com.hedvig.backoffice.config.feign.ExternalServiceBadRequestException
import com.hedvig.backoffice.config.feign.ExternalServiceUnauthorizedException
import com.hedvig.backoffice.services.gatekeeper.GatekeeperService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class GatekeeperFilter(
    private val gatekeeper: GatekeeperService
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
            val tokenInfo = gatekeeper.tokenInfo("Bearer $accessToken")
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
}
