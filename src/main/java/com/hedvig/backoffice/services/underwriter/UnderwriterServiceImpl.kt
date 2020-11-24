package com.hedvig.backoffice.services.underwriter

import com.fasterxml.jackson.databind.JsonNode
import com.hedvig.backoffice.config.feign.ExternalServiceBadRequestException
import com.hedvig.backoffice.services.underwriter.dtos.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.web.client.RestClientResponseException
import java.time.LocalDate
import java.util.*

private val logger: Logger = getLogger(UnderwriterServiceImpl::class.java)

class UnderwriterServiceImpl(
    private val underwriterClient: UnderwriterClient
) : UnderwriterService {

    private fun underwriterCreateQuoteFromAgreement(
        quoteRequest: QuoteFromAgreementRequestDto
    ): QuoteResponseDto? {
        return try {
            underwriterClient.createQuoteFromAgreement(quoteRequest)
        } catch (e: ExternalServiceBadRequestException) {
            logger.error("Failed to create quote", e)
            null
        }
    }

    override fun activateQuote(quoteId: UUID, activationDate: LocalDate?, terminationDate: LocalDate?): QuoteDto {
        logger.info("Activating quote $quoteId")
        val activatedQuote =
            underwriterClient.activateQuote(quoteId, ActivateQuoteRequestDto(activationDate, terminationDate))
        logger.info("Successfully activated quote $quoteId")
        return activatedQuote
    }

    override fun addAgreementFromQuote(
        quoteId: UUID,
        contractId: UUID?,
        activeFrom: LocalDate?,
        activeTo: LocalDate?,
        previousAgreementActiveTo: LocalDate?,
        token: String
    ): QuoteDto {
        logger.info("Adding agreement from quote=$quoteId")
        val addedQuote = underwriterClient.addAgreementFromQuote(
            AddAgreementFromQuoteRequest(
                quoteId = quoteId,
                contractId = contractId,
                activeFrom = activeFrom,
                activeTo = activeTo,
                previousAgreementActiveTo = previousAgreementActiveTo
            ),
            token
        )
        logger.info("Successfully added agreement=${addedQuote.signedProductId} from quote=$quoteId")
        return addedQuote
    }

    override fun getQuotes(memberId: String): List<QuoteDto> =
        underwriterClient.getQuotes(memberId)

    override fun getQuote(id: UUID): QuoteDto =
        underwriterClient.getQuote(id)

    override fun createQuoteFromAgreement(
        quoteRequest: QuoteFromAgreementRequestDto
    ): QuoteResponseDto {
        val createdQuote = underwriterCreateQuoteFromAgreement(quoteRequest.copy(underwritingGuidelinesBypassedBy = null))
            ?: underwriterCreateQuoteFromAgreement(quoteRequest)
            ?: throw RuntimeException("Could not create quote from agreement ${quoteRequest.agreementId}")

        return createdQuote
    }

    override fun signQuoteForNewContract(
        completeQuoteId: UUID,
        request: SignQuoteFromHopeRequestDto
    ): SignedQuoteResponseDto {
        try {
            val response = underwriterClient.signQuoteForNewContract(completeQuoteId, request)
            if (response.statusCode.is2xxSuccessful) {
                return SignedQuoteResponseDto.from(response.body as LinkedHashMap<String, String>)
            }
        } catch (ex: RestClientResponseException) {
            if (ex.rawStatusCode == 422) {
                logger.error("Cannot sign quote [QuoteId: $completeQuoteId] [ResponseBody: ${ex.responseBodyAsString}] [Exception: $ex]")
            }
            throw ex
        }
        throw IllegalStateException("Cannot sign quote [QuoteId: $completeQuoteId]")
    }

    override fun getSchemaForContractType(contractType: String): JsonNode? = try {
        underwriterClient.getSchemaForContractType(contractType).body
    } catch (exception: RestClientResponseException) {
        if (exception.rawStatusCode == 404) {
            null
        } else {
            throw exception
        }
    }

    override fun getSchemaByQuoteId(quoteId: UUID): JsonNode? = try {
        underwriterClient.getSchemaByQuoteId(quoteId).body
    } catch (exception: RestClientResponseException) {
        if (exception.rawStatusCode == 404) {
            null
        } else {
            throw exception
        }
    }

    override fun getSchemaDataByQuoteId(quoteId: UUID): JsonNode? = try {
        underwriterClient.getSchemaDataByQuoteId(quoteId).body
    } catch (exception: RestClientResponseException) {
        if (exception.rawStatusCode == 404) {
            null
        } else {
            throw exception
        }
    }

    override fun updateQuoteBySchemaData(quoteId: UUID, schemaData: JsonNode, underwritingGuidelinesBypassedBy: String?): QuoteResponseDto {
        try {
            return underwriterClient.updateQuoteBySchemaData(
                quoteId = quoteId,
                body = schemaData,
                underwritingGuidelinesBypassedBy = underwritingGuidelinesBypassedBy
            )
        } catch (exception: Exception) {
            logger.error("Unable to update quote with quoteId=$quoteId", exception)
            throw exception
        }
    }

    override fun createQuoteForMemberBySchemaData(memberId: String, schemaData: JsonNode, underwritingGuidelinesBypassedBy: String?): QuoteResponseDto {
        try {
            return underwriterClient.createQuoteForMemberBySchemaData(
                memberId = memberId,
                body = schemaData,
                underwritingGuidelinesBypassedBy = underwritingGuidelinesBypassedBy
            )
        } catch (exception: Exception) {
            logger.error("Unable to create quote for member with memberId=$memberId", exception)
            throw exception
        }
    }
}
