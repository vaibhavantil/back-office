package com.hedvig.backoffice.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import java.util.UUID

class GatekeeperUser(
  val id: UUID,
  username: String,
  authorities: Collection<GrantedAuthority>
) : User(username, "", authorities)
