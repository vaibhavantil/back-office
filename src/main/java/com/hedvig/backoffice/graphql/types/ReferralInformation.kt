package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.product_pricing.dto.RedeemedCampaignDto

data class ReferralInformation (
  val eligible: Boolean,
  val campaign: ReferralCampaign,
  val referredBy: MemberReferral?,
  val hasReferred: List<MemberReferral>,
  val redeemedCampaigns: List<RedeemedCampaignDto>
)

