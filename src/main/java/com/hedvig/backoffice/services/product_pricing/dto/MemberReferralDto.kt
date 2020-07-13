package com.hedvig.backoffice.services.product_pricing.dto

import com.hedvig.backoffice.graphql.types.MemberReferral

data class MemberReferralDto(
  val memberId: String,
  val name: String?,
  val status: String,
  val incentive: IncentiveDto
) {
    fun toGraphqlType() = MemberReferral(
      memberId = this.memberId,
      name = this.name,
      status = this.status,
      incentive = this.incentive
    )
}
