package com.hedvig.backoffice.services.gatekeeper

import com.hedvig.backoffice.security.dto.TokenInfo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "gatekeeper", url = "\${gatekeeper.baseUrl}")
interface GatekeeperClient {
    @GetMapping("/oauth2/tokeninfo")
    fun tokenInfo(@RequestHeader("Authorization") authorization: String): TokenInfo
}
