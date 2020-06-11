package com.hedvig.backoffice.services.product_pricing.dto

import java.time.Instant

data class PartnerCampaignSearchResponse(
  val id: String,
  val campaignCode: String,
  val partnerId: String,
  val partnerName: String,
  val validFrom: Instant?,
  val validTo: Instant?,
  val incentive: IncentiveDto
)
