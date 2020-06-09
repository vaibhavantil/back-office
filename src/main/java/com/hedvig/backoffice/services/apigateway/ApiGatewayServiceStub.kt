package com.hedvig.backoffice.services.apigateway

import org.springframework.stereotype.Service

class ApiGatewayServiceStub : ApiGatewayService {
  override fun generatePaymentsLink(memberId: String): GeneratePaymentsLinkResponseDto =
    GeneratePaymentsLinkResponseDto(
      url = "http://localhost:8080/se/new-member/connect-payment/direct#exchange-token=blah"
    )
}
