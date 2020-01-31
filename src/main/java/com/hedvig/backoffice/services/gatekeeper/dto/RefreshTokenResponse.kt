package com.hedvig.backoffice.services.gatekeeper.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class RefreshTokenResponse(
  @JsonProperty("access_token")
  val accessToken: String,
  @JsonProperty("refresh_token")
  val refreshToken: String
)
