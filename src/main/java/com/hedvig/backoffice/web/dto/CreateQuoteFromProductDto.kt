package com.hedvig.backoffice.web.dto

import java.util.UUID
import com.hedvig.backoffice.graphql.types.QuoteFromProductInput
import com.hedvig.backoffice.services.underwriter.dtos.QuoteData

data class CreateQuoteFromProductDto(
  val incompleteHouseQuoteData: QuoteData.HouseQuoteData?,
  val incompleteApartmentQuoteData: QuoteData.ApartmentQuoteData?,
  val originatingProductId: UUID?,
  val currentInsurer: String?
) {
  companion object {
    @JvmStatic
    fun from(dto: QuoteFromProductInput): CreateQuoteFromProductDto =
      CreateQuoteFromProductDto(
        incompleteHouseQuoteData = dto.incompleteHouseQuoteData?.let((QuoteData.HouseQuoteData)::from),
        incompleteApartmentQuoteData = dto.incompleteApartmentQuoteData?.let((QuoteData.ApartmentQuoteData)::from),
        currentInsurer = dto.currentInsurer,
        originatingProductId = dto.originatingProductId
      )
  }
}
