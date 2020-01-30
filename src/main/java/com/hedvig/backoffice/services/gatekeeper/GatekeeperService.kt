package com.hedvig.backoffice.services.gatekeeper

import com.hedvig.backoffice.security.dto.TokenInfo

interface GatekeeperService {
  fun tokenInfo(authorization: String): TokenInfo
}
