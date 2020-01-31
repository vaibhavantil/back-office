package com.hedvig.backoffice.services.gatekeeper

import com.hedvig.backoffice.security.dto.TokenInfo
import com.hedvig.backoffice.services.gatekeeper.dto.RefreshTokenResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GatekeeperServiceImpl(private val gatekeeperClient: GatekeeperClient) : GatekeeperService {

  @Value("\${gatekeeper.clientId:clientId}")
  lateinit var clientId: String

  @Value("\${gatekeeper.clientSecret:clientSecret}")
  lateinit var clientSecret: String

  override fun tokenInfo(authorization: String): TokenInfo =
    gatekeeperClient.tokenInfo(authorization)

  override fun refreshToken(refreshToken: String): RefreshTokenResponse =
    gatekeeperClient.refreshToken(
      clientId = clientId,
      clientSecret = clientSecret,
      grantType = "refresh_token",
      refreshToken = refreshToken
    )
}
