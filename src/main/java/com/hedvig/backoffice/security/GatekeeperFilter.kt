package com.hedvig.backoffice.security

import com.hedvig.backoffice.config.feign.ExternalServiceBadRequestException
import com.hedvig.backoffice.config.feign.ExternalServiceUnauthorizedException
import com.hedvig.backoffice.services.gatekeeper.GatekeeperService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class GatekeeperFilter(private val gatekeeper: GatekeeperService) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val accessToken = request.cookies?.find { cookie -> cookie.name == "_hvg_at" }?.value

        try {
            if (accessToken == null) {
                SecurityContextHolder.getContext().authentication = null
            } else {
                val tokenInfo = gatekeeper.tokenInfo("Bearer $accessToken")
                if (tokenInfo.scopes.contains("hope:read")) {
                    SecurityContextHolder.getContext().authentication = GatekeeperAuthentication(tokenInfo, accessToken)
                } else {
                    logger.warn("User ${tokenInfo.subject} tried to access hope but is lacking the \"hope:read\" scope")
                }
            }
        } catch (e: ExternalServiceUnauthorizedException) {
            // we're not authenticated
            SecurityContextHolder.getContext().authentication = null
        } catch (e: ExternalServiceBadRequestException) {
            // we're not authenticated
            SecurityContextHolder.getContext().authentication = null
        }

        chain.doFilter(request, response);
    }
}
