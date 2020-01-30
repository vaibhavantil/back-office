package com.hedvig.backoffice.services.underwriter

import com.hedvig.backoffice.services.underwriter.dtos.CreateQuoteFromProductDto
import com.hedvig.backoffice.services.underwriter.dtos.ProductType
import com.hedvig.backoffice.services.underwriter.dtos.QuoteData
import com.hedvig.backoffice.services.underwriter.dtos.QuoteDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteInitiatedFrom
import com.hedvig.backoffice.services.underwriter.dtos.QuoteInputDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteResponseDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteState
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

internal val QUOTE_DTO_MOCK = QuoteDto(
  id = UUID.randomUUID(),
  memberId = "12345",
  createdAt = Instant.now(),
  price = BigDecimal.valueOf(142),
  productType = ProductType.APARTMENT,
  state = QuoteState.QUOTED,
  initiatedFrom = QuoteInitiatedFrom.RAPIO,
  attributedTo = "HEDVIG",
  data = QuoteData.ApartmentQuoteData(
    UUID.randomUUID(),
    ssn = "191212121212",
    lastName = "Last",
    livingSpace = 2,
    city = "Storstan",
    zipCode = "12345",
    householdSize = 3,
    subType = com.hedvig.backoffice.services.product_pricing.dto.ProductType.BRF,
    firstName = "First",
    street = "Storgatan 1"
  ),
  originatingProductId = UUID.randomUUID(),
  signedProductId = null,
  currentInsurer = null,
  isComplete = true,
  breachedUnderwritingGuidelines = emptyList(),
  validity = 86400 * 30,
  startDate = null
)

class UnderwriterServiceStub : UnderwriterService {
  override fun createAndCompleteQuote(
        memberId: String,
        quoteDto: CreateQuoteFromProductDto,
        underwritingGuidelinesBypassedBy: String?
    ): QuoteResponseDto =
    QuoteResponseDto(UUID.randomUUID())

  override fun updateQuote(quoteId: UUID, quoteDto: QuoteInputDto, underwritingGuidelinesBypassedBy: String?): QuoteDto = QUOTE_DTO_MOCK

  override fun activateQuote(quoteId: UUID, activationDate: LocalDate?, terminationDate: LocalDate?): QuoteDto = QUOTE_DTO_MOCK

  override fun getQuotes(memberId: String): List<QuoteDto> = listOf(QUOTE_DTO_MOCK)

  override fun getQuote(id: UUID): QuoteDto = QUOTE_DTO_MOCK
}