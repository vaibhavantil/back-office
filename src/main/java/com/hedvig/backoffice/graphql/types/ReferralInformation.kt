package com.hedvig.backoffice.graphql.types

data class ReferralInformation (
  val eligible: Boolean,
  val campaign: ReferralCampaign,
  val referredBy: MemberReferral?,
  val hasReferred: List<MemberReferral>
)

