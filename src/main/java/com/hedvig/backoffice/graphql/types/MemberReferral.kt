package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.product_pricing.dto.IncentiveDto

data class MemberReferral(
  val memberId: String,
  val name: String?,
  val status: String,
  val incentive: IncentiveDto
)
