package com.hedvig.backoffice.services.product_pricing.dto

import java.time.LocalDate

data class ManualRedeemCampaignRequest (
  val campaignCode: String,
  val activationDate: LocalDate
)
