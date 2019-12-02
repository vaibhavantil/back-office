package com.hedvig.backoffice.services.product_pricing.dto

import java.time.LocalDate

data class MemberSearchResultDTOExtended(
  val memberId: Long,
  val firstActiveFrom: LocalDate?,
  val lastActiveTo: LocalDate?,
  val currentInsuranceStatus: String?,
  val householdSize: Int?,
  val livingSpace: Int?
)
