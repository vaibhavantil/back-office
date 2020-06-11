package com.hedvig.backoffice.services.apigateway

interface ApiGatewayService {
  fun generatePaymentsLink(memberId: String): GeneratePaymentsLinkResponseDto
}
