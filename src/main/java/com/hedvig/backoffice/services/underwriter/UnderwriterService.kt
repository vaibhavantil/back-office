package com.hedvig.backoffice.services.underwriter

import com.hedvig.backoffice.services.underwriter.dtos.QuoteDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteInputDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteResponseDto
import com.hedvig.backoffice.services.underwriter.dtos.CreateQuoteFromProductDto
import java.time.LocalDate
import java.util.UUID

interface UnderwriterService {
  fun createAndCompleteQuote(memberId: String, quoteDto: CreateQuoteFromProductDto, underwritingGuidelinesBypassedBy: String?): QuoteResponseDto
  fun updateQuote(quoteId: UUID, quoteDto: QuoteInputDto, underwritingGuidelinesBypassedBy: String?): QuoteDto
  fun activateQuote(quoteId: UUID, activationDate: LocalDate?, terminationDate: LocalDate?): QuoteDto
  fun getQuotes(memberId: String): List<QuoteDto>
  fun getQuote(id: UUID): QuoteDto
}
