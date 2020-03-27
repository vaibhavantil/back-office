package com.hedvig.backoffice.services.priceEngine

import com.hedvig.backoffice.config.feign.FeignConfig
import com.hedvig.backoffice.services.priceEngine.dto.AddNorwegianPostalCodeRequest
import com.hedvig.backoffice.services.priceEngine.dto.CreateNorwegianGripenRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "price-engine", url = "\${priceEngine.baseUrl:price-engine}", configuration = [FeignConfig::class])
interface PriceEngineClient {
  @PostMapping("/_/norwegian/gripen/create")
  fun createNorwegianGripenPriceEngine(
    @RequestBody createNorwegianGripenRequest: CreateNorwegianGripenRequest,
    @RequestHeader("Authorization") token: String
  )

  @PostMapping("/_/norwegian/postal/codes/add")
  fun addNorwegianPostalCodes(
    @RequestBody request: AddNorwegianPostalCodeRequest
  )
}
