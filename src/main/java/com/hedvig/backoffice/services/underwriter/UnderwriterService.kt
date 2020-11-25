package com.hedvig.backoffice.services.underwriter

import com.fasterxml.jackson.databind.JsonNode
import com.hedvig.backoffice.services.underwriter.dtos.QuoteDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteFromAgreementRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteResponseDto
import com.hedvig.backoffice.services.underwriter.dtos.SignQuoteFromHopeRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.SignedQuoteResponseDto
import java.time.LocalDate
import java.util.UUID

interface UnderwriterService {
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
        previousAgreementActiveTo: LocalDate?,
        token: String
    ): QuoteDto

    fun getQuotes(memberId: String): List<QuoteDto>
    fun getQuote(id: UUID): QuoteDto
    fun createQuoteFromAgreement(quoteRequest: QuoteFromAgreementRequestDto): QuoteResponseDto
    fun signQuoteForNewContract(completeQuoteId: UUID, request: SignQuoteFromHopeRequestDto): SignedQuoteResponseDto

    fun getSchemaForContractType(contractType: String): JsonNode?
    fun getSchemaByQuoteId(quoteId: UUID): JsonNode?
    fun getSchemaDataByQuoteId(quoteId: UUID): JsonNode?
    fun updateQuoteBySchemaData(quoteId: UUID, schemaData: JsonNode, underwritingGuidelinesBypassedBy: String?): QuoteResponseDto
    fun createQuoteForMemberBySchemaData(memberId: String, schemaData: JsonNode, underwritingGuidelinesBypassedBy: String?): QuoteResponseDto
}
