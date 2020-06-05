package com.hedvig.backoffice.services.apigateway

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ApiGatewayServiceImpl(
  private val apiGatewayServiceClient: ApiGatewayServiceClient,
  @Value("\${api-gateway.token}")
  private val apiGatewayToken: String
) : ApiGatewayService {
  override fun generatePaymentsLink(memberId: String): GeneratePaymentsLinkResponseDto =
    apiGatewayServiceClient.setupPaymentLink(
      token = apiGatewayToken,
      dto = GeneratePaymentsLinkRequestDto(
        memberId = memberId
      )
    )
      .body
}
