package com.hedvig.backoffice.services.apigateway

import com.hedvig.backoffice.services.members.MemberService
import org.springframework.beans.factory.annotation.Value

class ApiGatewayServiceImpl(
    private val apiGatewayServiceClient: ApiGatewayServiceClient,
    private val memberService: MemberService,
    @Value("\${apiGateway.token}")
    private val apiGatewayToken: String
) : ApiGatewayService {
    override fun generatePaymentsLink(memberId: String): GeneratePaymentsLinkResponseDto {
        val countryCode = memberService.getPickedLocaleByMemberId(memberId).countryCode

        return apiGatewayServiceClient.setupPaymentLink(
            token = apiGatewayToken,
            dto = GeneratePaymentsLinkRequestDto(
                memberId = memberId,
                countryCode = countryCode
            )
        ).body
    }
}
