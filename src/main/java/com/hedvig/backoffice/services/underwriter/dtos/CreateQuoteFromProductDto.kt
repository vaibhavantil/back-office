package com.hedvig.backoffice.services.underwriter.dtos

import java.util.UUID
import com.hedvig.backoffice.graphql.types.QuoteFromProductInput

data class CreateQuoteFromProductDto(
  val incompleteHouseQuoteData: QuoteData.HouseData?,
  val incompleteApartmentQuoteData: QuoteData.ApartmentData?,
  val norwegianHomeContentData: QuoteData.NorwegianHomeContentData?,
  val norwegianTravelData: QuoteData.NorwegianTravelData?,
  val originatingProductId: UUID?,
  val currentInsurer: String?
) {
  companion object {
    @JvmStatic
    fun from(dto: QuoteFromProductInput): CreateQuoteFromProductDto =
      CreateQuoteFromProductDto(
        incompleteHouseQuoteData = dto.incompleteHouseQuoteData?.let((QuoteData.HouseData)::from),
        incompleteApartmentQuoteData = dto.incompleteApartmentQuoteData?.let((QuoteData.ApartmentData)::from),
        norwegianHomeContentData = dto.norwegianHomeContentQuoteData?.let((QuoteData.NorwegianHomeContentData)::from),
        norwegianTravelData = dto.norwegianTravelQuoteData?.let((QuoteData.NorwegianTravelData)::from),
        currentInsurer = dto.currentInsurer,
        originatingProductId = dto.originatingProductId
      )
  }
}
