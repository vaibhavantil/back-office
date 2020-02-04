package com.hedvig.backoffice.security.dto

import java.util.UUID

data class TokenInfo(
  val id: UUID,
  val subject: String,
  val scopes: Set<String>,
  val role: Role
)
