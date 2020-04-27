package com.hedvig.backoffice.services.underwriter

import com.hedvig.backoffice.config.feign.ExternalServiceBadRequestException
import com.hedvig.backoffice.services.members.MemberService
import com.hedvig.backoffice.services.underwriter.dtos.*
import feign.FeignException
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.time.LocalDate
import java.util.UUID

private val logger: Logger = getLogger(UnderwriterServiceImpl::class.java)

class UnderwriterServiceImpl(
  private val underwriterClient: UnderwriterClient,
  private val memberService: MemberService
) : UnderwriterService {
  override fun createAndCompleteQuote(memberId: String, quoteDto: CreateQuoteFromProductDto, underwritingGuidelinesBypassedBy: String?): QuoteResponseDto {
    val member = memberService.findByMemberId(memberId, "")
    logger.info("Creating quote for member $memberId")
    val quoteRequestDto = QuoteRequestDto(
        firstName = member.firstName!!,
        lastName = member.lastName!!,
        ssn = member.ssn,
        memberId = memberId,
        originatingProductId = quoteDto.originatingProductId,
        currentInsurer = quoteDto.currentInsurer,
        birthDate = member.birthDate,
        productType = if (quoteDto.incompleteApartmentQuoteData != null) {
          ProductType.APARTMENT
        } else {
          ProductType.HOUSE
        },
        incompleteApartmentQuoteData = quoteDto.incompleteApartmentQuoteData?.let((IncompleteApartmentQuoteDataDto)::from),
        incompleteHouseQuoteData = quoteDto.incompleteHouseQuoteData?.let((IncompleteHouseQuoteDataDto)::from),
        norwegianHomeContentsData = quoteDto.norwegianHomeContentQuoteData?.let((IncompleteNorwegianHomeContentsQuoteDataDto)::from),
        norwegianTravelData = quoteDto.norwegianTravelQuoteData?.let((IncompleteNorwegianTravelQuoteDataDto)::from),
        quotingPartner = null,
        underwritingGuidelinesBypassedBy = null
    )

    val createdQuote = underwriterCreateQuote(quoteRequestDto)
      ?: underwriterCreateQuote(quoteRequestDto.copy(underwritingGuidelinesBypassedBy = underwritingGuidelinesBypassedBy))
      ?: throw RuntimeException("Could not create quote from product ${quoteDto.originatingProductId}")

    return createdQuote
  }

  private fun underwriterCreateQuote(quoteRequestDto: QuoteRequestDto): QuoteResponseDto? {
    return try {
      underwriterClient.createQuote(
        quoteRequestDto
      )
    } catch (e: ExternalServiceBadRequestException) {
      logger.error("Failed to complete quote", e)
      null
    }
  }

  private fun underwriterCreateQuoteFromBackOffice(
    quoteRequest: QuoteRequestFromBackOfficeDto
  ): QuoteResponseDto? {
    return try {
      underwriterClient.createQuoteFromBackOffice(quoteRequest)
    } catch (e: ExternalServiceBadRequestException) {
      logger.error("Failed to complete quote", e)
      null
    }
  }

  override fun updateQuote(quoteId: UUID, quoteDto: QuoteInputDto, underwritingGuidelinesBypassedBy: String?): QuoteDto {
    logger.info("Updating quote $quoteId")
    val updatedQuote = underwriterClient.updateQuote(quoteId, quoteDto, underwritingGuidelinesBypassedBy)
    logger.info("Successfully updated quote $quoteId")

    if (!updatedQuote.isComplete) {
      logger.info("Quote updated but was incomplete, trying to complete it")
      try {
        underwriterClient.completeQuote(quoteId, underwritingGuidelinesBypassedBy)
      } catch (e: FeignException) {
        logger.error("Failed to complete updated quote", e)
        // Noop
      }
    }

    return underwriterClient.getQuote(quoteId)
  }

  override fun activateQuote(quoteId: UUID, activationDate: LocalDate?, terminationDate: LocalDate?): QuoteDto {
    logger.info("Activating quote $quoteId")
    val activatedQuote = underwriterClient.activateQuote(quoteId, ActivateQuoteRequestDto(activationDate, terminationDate))
    logger.info("Successfully activated quote $quoteId")
    return activatedQuote
  }

  override fun addAgreementFromQuote(quoteId: UUID, contractId: UUID?, activeFrom: LocalDate?, activeTo: LocalDate?, previousAgreementActiveTo: LocalDate?): QuoteDto {
    logger.info("Adding agreement from quote=$quoteId")
    val addedQuote = underwriterClient.addAgreementFromQuote(AddAgreementFromQuoteRequest(
      quoteId = quoteId,
      contractId = contractId,
      activeFrom = activeFrom,
      activeTo = activeTo,
      previousAgreementActiveTo = previousAgreementActiveTo
    ))
    logger.info("Successfully added agreement=${addedQuote.signedProductId} from quote=$quoteId")
    return addedQuote
  }

  override fun getQuotes(memberId: String): List<QuoteDto> =
    underwriterClient.getQuotes(memberId)

  override fun getQuote(id: UUID): QuoteDto =
    underwriterClient.getQuote(id)

  override fun createQuoteFromBackOffice(
    quoteRequest: QuoteRequestFromBackOfficeDto
  ): QuoteResponseDto {
    val createdQuote = underwriterCreateQuoteFromBackOffice(quoteRequest.copy(underwritingGuidelinesBypassedBy = null))
      ?: underwriterCreateQuoteFromBackOffice(quoteRequest)
      ?: throw RuntimeException("Could not create quote from agreement ${quoteRequest.agreementId}")

    return createdQuote
  }
}
