package com.hedvig.backoffice.graphql.resolvers;

import com.hedvig.backoffice.services.product_pricing.dto.IncentiveDto
import com.hedvig.backoffice.services.product_pricing.dto.PartnerCampaignSearchResponse
import java.time.Instant

data class VoucherCampaign (
  val id: String,
  val campaignCode: String,
  val partnerId: String,
  val partnerName: String,
  val validFrom: Instant?,
  val validTo: Instant?,
  val incentive: IncentiveDto
) {

  companion object {
    fun from(partnerCampaignSearchResponse: PartnerCampaignSearchResponse): VoucherCampaign {
      return VoucherCampaign(
        id = partnerCampaignSearchResponse.id,
        campaignCode = partnerCampaignSearchResponse.campaignCode,
        partnerId = partnerCampaignSearchResponse.partnerId,
        partnerName = partnerCampaignSearchResponse.partnerName,
        validFrom = partnerCampaignSearchResponse.validFrom,
        validTo = partnerCampaignSearchResponse.validTo,
        incentive = partnerCampaignSearchResponse.incentive
      )
    }
  }
}
