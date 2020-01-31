package com.hedvig.backoffice.services.gatekeeper

import com.hedvig.backoffice.security.dto.TokenInfo
import com.hedvig.backoffice.services.gatekeeper.dto.RefreshTokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "gatekeeper", url = "\${gatekeeper.baseUrl}")
interface GatekeeperClient {
  @GetMapping("/oauth2/tokeninfo")
  fun tokenInfo(@RequestHeader("Authorization") authorization: String): TokenInfo

  @PostMapping()
  fun refreshToken(
    @RequestHeader("client_id") clientId: String,
    @RequestHeader("client_secret") clientSecret: String,
    @RequestHeader("grant_type") grantType: String,
    @RequestHeader("refresh_token") refreshToken: String
  ): RefreshTokenResponse
}
