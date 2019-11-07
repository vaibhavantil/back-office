package com.hedvig.backoffice.services.underwriter

import com.hedvig.backoffice.services.underwriter.dtos.QuoteDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteResponseDto
import com.hedvig.backoffice.web.dto.CreateQuoteFromProductDto
import java.time.LocalDate
import java.util.UUID

interface UnderwriterService {
  fun createAndCompleteQuote(memberId: String, quoteDto: CreateQuoteFromProductDto): QuoteResponseDto
  fun activateQuote(quoteId: UUID, activationDate: LocalDate?, terminationDate: LocalDate?): QuoteDto
  fun getQuotes(memberId: String): List<QuoteDto>
}
