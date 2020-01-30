package com.hedvig.backoffice.security.dto

data class TokenInfo(
    val subject: String,
    val scopes: Set<String>,
    val role: Role
)
