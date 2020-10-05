package com.hedvig.backoffice.services.underwriter

import com.fasterxml.jackson.databind.JsonNode
import com.hedvig.backoffice.services.underwriter.dtos.*
import java.time.LocalDate
import java.util.*

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


  fun getSchemaForContractType(contractType: ContractType): JsonNode?
  fun getSchemaByQuoteId(quoteId: UUID): JsonNode?
  fun getSchemaWithDataByQuoteId(quoteId: UUID): JsonNode?
  fun updateQuoteBySchemaWithData(quoteId: UUID, schemaData: JsonNode, underwritingGuidelinesBypassedBy: String?): QuoteResponseDto
  fun createQuoteForMemberBySchemaWithData(memberId: String, schemaData: JsonNode, underwritingGuidelinesBypassedBy: String?): QuoteResponseDto
}
