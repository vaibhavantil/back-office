package com.hedvig.backoffice.services.underwriter.dtos

import com.hedvig.backoffice.graphql.types.QuoteFromProductInput
import java.util.UUID

data class CreateQuoteFromProductDto(
  val incompleteHouseQuoteData: QuoteData.HouseData?,
  val incompleteApartmentQuoteData: QuoteData.ApartmentData?,
  val norwegianHomeContentQuoteData: QuoteData.NorwegianHomeContentData?,
  val norwegianTravelQuoteData: QuoteData.NorwegianTravelData?,
  val originatingProductId: UUID?,
  val currentInsurer: String?
) {
  companion object {
    @JvmStatic
    fun from(dto: QuoteFromProductInput): CreateQuoteFromProductDto =
      CreateQuoteFromProductDto(
        incompleteHouseQuoteData = dto.incompleteHouseQuoteData?.let((QuoteData.HouseData)::from),
        incompleteApartmentQuoteData = dto.incompleteApartmentQuoteData?.let((QuoteData.ApartmentData)::from),
        norwegianHomeContentQuoteData = dto.norwegianHomeContentQuoteData?.let((QuoteData.NorwegianHomeContentData)::from),
        norwegianTravelQuoteData = dto.norwegianTravelQuoteData?.let((QuoteData.NorwegianTravelData)::from),
        currentInsurer = dto.currentInsurer,
        originatingProductId = dto.originatingProductId
      )
  }
}
