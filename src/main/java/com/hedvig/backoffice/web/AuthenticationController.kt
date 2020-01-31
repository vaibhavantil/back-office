package com.hedvig.backoffice.web

import com.hedvig.backoffice.services.gatekeeper.GatekeeperService
import com.hedvig.backoffice.services.gatekeeper.dto.RefreshTokenResponse
import com.hedvig.backoffice.web.dto.RefreshTokenDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
  private val gatekeeperService: GatekeeperService
){

  @PostMapping("/refresh/token")
  fun refreshToken(refreshTokenDto: RefreshTokenDto): ResponseEntity<RefreshTokenResponse> =
    ResponseEntity.ok(gatekeeperService.refreshToken(refreshTokenDto.refreshToken))
}

