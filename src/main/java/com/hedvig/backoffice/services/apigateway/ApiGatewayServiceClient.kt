package com.hedvig.backoffice.services.apigateway;

import com.hedvig.backoffice.config.feign.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
  name = "apigateway-service",
  url = "\${apiGateway.baseUrl}",
  configuration = [FeignConfig::class]
)
interface ApiGatewayServiceClient {
  @PostMapping(value = ["/_/setupPaymentLink/create"])
  fun setupPaymentLink(
    @RequestHeader token: String,
    @RequestBody dto: GeneratePaymentsLinkRequestDto
  ): ResponseEntity<GeneratePaymentsLinkResponseDto>
}
