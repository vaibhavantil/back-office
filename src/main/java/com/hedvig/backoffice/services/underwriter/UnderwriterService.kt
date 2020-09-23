package com.hedvig.backoffice.services.underwriter

import com.fasterxml.jackson.databind.JsonNode
import com.hedvig.backoffice.services.underwriter.dtos.CreateQuoteFromProductDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteForNewContractRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteFromAgreementRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteInputDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteResponseDto
import com.hedvig.backoffice.services.underwriter.dtos.SignQuoteFromHopeRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.SignedQuoteResponseDto
import java.time.LocalDate
import java.util.UUID

interface UnderwriterService {
  fun createAndCompleteQuote(
    memberId: String,
    quoteDto: CreateQuoteFromProductDto,
    underwritingGuidelinesBypassedBy: String?
  ): QuoteResponseDto

  fun updateQuote(
    quoteId: UUID,
    quoteDto: QuoteInputDto,
    underwritingGuidelinesBypassedBy: String?
  ): QuoteDto

  fun activateQuote(
    quoteId: UUID,
    activationDate: LocalDate?,
    terminationDate: LocalDate?
  ): QuoteDto

  fun addAgreementFromQuote(
    quoteId: UUID,
    contractId: UUID?,
    activeFrom: LocalDate?,
    activeTo: LocalDate?,
    previousAgreementActiveTo: LocalDate?
  ): QuoteDto

  fun getQuotes(memberId: String): List<QuoteDto>
  fun getQuote(id: UUID): QuoteDto
  fun createQuoteFromAgreement(quoteRequest: QuoteFromAgreementRequestDto): QuoteResponseDto
  fun createQuoteForNewContract(request: QuoteForNewContractRequestDto): QuoteResponseDto
  fun signQuoteForNewContract(completeQuoteId: UUID, request: SignQuoteFromHopeRequestDto): SignedQuoteResponseDto


  fun getSchemaFromQuote(id: UUID): JsonNode?
}
