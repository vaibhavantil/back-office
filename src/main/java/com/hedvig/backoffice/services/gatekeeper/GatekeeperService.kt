package com.hedvig.backoffice.services.gatekeeper

import com.hedvig.backoffice.security.dto.TokenInfo
import com.hedvig.backoffice.services.gatekeeper.dto.RefreshTokenResponse

interface GatekeeperService {
  fun tokenInfo(authorization: String): TokenInfo
  fun refreshToken(refreshToken: String): RefreshTokenResponse
}

