package com.hedvig.backoffice.services.product_pricing.dto


data class ReferralInformationDto (
  val code: String,
  val incentive: IncentiveDto?,
  val referredBy: MemberReferralDto?,
  val hasReferred: List<MemberReferralDto>
)
