package com.hedvig.backoffice.services.apigateway

import com.hedvig.backoffice.services.members.MemberService
import com.neovisionaries.i18n.CountryCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

class ApiGatewayServiceImpl(
  private val apiGatewayServiceClient: ApiGatewayServiceClient,
  private val memberService: MemberService,
  @Value("\${apiGateway.token}")
  private val apiGatewayToken: String
) : ApiGatewayService {
  override fun generatePaymentsLink(memberId: String): GeneratePaymentsLinkResponseDto {
    val pickedLocale = memberService.findPickedLocaleByMemberId(memberId)
   return apiGatewayServiceClient.setupPaymentLink(
      token = apiGatewayToken,
      dto = GeneratePaymentsLinkRequestDto(
        memberId = memberId,
        countryCode = CountryCode.getByLocale(pickedLocale.pickedLocale.locale)
      )
    )
      .body
  }
}
