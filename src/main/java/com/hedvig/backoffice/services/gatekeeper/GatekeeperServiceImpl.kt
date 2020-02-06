package com.hedvig.backoffice.services.gatekeeper

import com.hedvig.backoffice.security.dto.TokenInfo
import org.springframework.stereotype.Service

@Service
class GatekeeperServiceImpl(private val gatekeeperClient: GatekeeperClient): GatekeeperService {
  override fun tokenInfo(authorization: String): TokenInfo =
    gatekeeperClient.tokenInfo(authorization)
}
