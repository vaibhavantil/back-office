package com.hedvig.backoffice.web.dto

import com.hedvig.backoffice.services.product_pricing.dto.ExtraBuildingDTO
import com.hedvig.backoffice.services.product_pricing.dto.ProductType
import com.hedvig.backoffice.services.underwriter.dtos.IncompleteApartmentQuoteDataDto
import com.hedvig.backoffice.services.underwriter.dtos.IncompleteHouseQuoteDataDto
import java.time.LocalDate
import java.util.UUID

data class CreateQuoteFromProductDto(
  val incompleteHouseQuoteData: IncompleteHouseQuoteDataDto?,
  val incompleteApartmentQuoteData: IncompleteApartmentQuoteDataDto?,
  val originatingProductId: UUID?,
  val currentInsurer: String?
)
