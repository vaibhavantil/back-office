package com.hedvig.backoffice.security

import com.hedvig.backoffice.security.dto.TokenInfo
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority

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
