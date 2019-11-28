package com.hedvig.backoffice.services.underwriter

import com.hedvig.backoffice.services.members.MemberService
import com.hedvig.backoffice.services.underwriter.dtos.ActivateQuoteRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.CreateQuoteFromProductDto
import com.hedvig.backoffice.services.underwriter.dtos.IncompleteApartmentQuoteDataDto
import com.hedvig.backoffice.services.underwriter.dtos.IncompleteHouseQuoteDataDto
import com.hedvig.backoffice.services.underwriter.dtos.ProductType
import com.hedvig.backoffice.services.underwriter.dtos.QuoteDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteInputDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteResponseDto
import feign.FeignException
import io.sentry.Sentry
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.time.LocalDate
import java.util.UUID

private val logger: Logger = getLogger(UnderwriterServiceImpl::class.java)

class UnderwriterServiceImpl(
  private val underwriterClient: UnderwriterClient,
  private val memberService: MemberService
) : UnderwriterService {
  override fun createAndCompleteQuote(memberId: String, quoteDto: CreateQuoteFromProductDto): QuoteResponseDto {
    val member = memberService.findByMemberId(memberId, "")
    logger.info("Creating quote for member $memberId")
    val createdQuote = underwriterClient.createQuote(
      QuoteRequestDto(
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
        quotingPartner = null
      )
    )

    logger.info("Created quote ${createdQuote.id} for member $memberId, trying to complete it")

    try {
      underwriterClient.completeQuote(createdQuote.id, null)
    } catch (e: FeignException) {
      logger.error("Failed to complete quote ${createdQuote.id}", e)
      // noop
    }

    return createdQuote
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
        Sentry.capture(e)
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

  override fun getQuotes(memberId: String): List<QuoteDto> =
    underwriterClient.getQuotes(memberId)

  override fun getQuote(id: UUID): QuoteDto =
    underwriterClient.getQuote(id)
}
