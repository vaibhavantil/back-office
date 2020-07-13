package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.product_pricing.dto.IncentiveDto

data class ReferralCampaign(
  val code: String,
  val incentive: IncentiveDto?
)
