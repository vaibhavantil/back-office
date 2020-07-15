package com.hedvig.backoffice.services.product_pricing.dto

data class RedeemedCampaignDto (
  val code: String,
  val type: String,
  val incentive: IncentiveDto,
  val redemptionState: RedemptionStateDto
)
