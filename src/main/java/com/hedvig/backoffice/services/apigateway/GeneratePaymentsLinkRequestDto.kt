package com.hedvig.backoffice.services.apigateway

import com.neovisionaries.i18n.CountryCode

data class GeneratePaymentsLinkRequestDto(
  val memberId: String,
  val countryCode: CountryCode
)
