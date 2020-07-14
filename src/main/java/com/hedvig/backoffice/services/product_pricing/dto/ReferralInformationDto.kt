package com.hedvig.backoffice.services.product_pricing.dto

import com.hedvig.backoffice.graphql.types.MemberReferral

data class ReferralInformationDto (
  val code: String,
  val incentive: IncentiveDto?,
  val referredBy: MemberReferralDto?,
  val hasReferred: List<MemberReferralDto>
)
