package com.hedvig.backoffice.services.underwriter

import com.hedvig.backoffice.config.feign.ExternalServiceBadRequestException
import com.hedvig.backoffice.services.members.MemberService
import com.hedvig.backoffice.services.underwriter.dtos.ActivateQuoteRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.AddAgreementFromQuoteRequest
import com.hedvig.backoffice.services.underwriter.dtos.CreateQuoteFromProductDto
import com.hedvig.backoffice.services.underwriter.dtos.IncompleteApartmentQuoteDataDto
import com.hedvig.backoffice.services.underwriter.dtos.IncompleteHouseQuoteDataDto
import com.hedvig.backoffice.services.underwriter.dtos.IncompleteNorwegianHomeContentsQuoteDataDto
import com.hedvig.backoffice.services.underwriter.dtos.IncompleteNorwegianTravelQuoteDataDto
import com.hedvig.backoffice.services.underwriter.dtos.ProductType
import com.hedvig.backoffice.services.underwriter.dtos.QuoteDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteForNewContractRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteFromAgreementRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteInputDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteResponseDto
import com.hedvig.backoffice.services.underwriter.dtos.SignQuoteFromHopeRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.SignedQuoteResponseDto
import feign.FeignException
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.web.client.RestClientResponseException
import java.lang.IllegalStateException
import java.time.LocalDate
import java.util.UUID

private val logger: Logger = getLogger(UnderwriterServiceImpl::class.java)

class UnderwriterServiceImpl(
  private val underwriterClient: UnderwriterClient,
  private val memberService: MemberService
) : UnderwriterService {
  override fun createAndCompleteQuote(
    memberId: String,
    quoteDto: CreateQuoteFromProductDto,
    underwritingGuidelinesBypassedBy: String?
  ): QuoteResponseDto {
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
        norwegianHomeContentsData = quoteDto.norwegianHomeContentData?.let((IncompleteNorwegianHomeContentsQuoteDataDto)::from),
        norwegianTravelData = quoteDto.norwegianTravelData?.let((IncompleteNorwegianTravelQuoteDataDto)::from),
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

  private fun underwriterCreateQuoteForNewContract(
    quoteRequest: QuoteForNewContractRequestDto
  ): QuoteResponseDto? {
    return try {
      underwriterClient.createQuoteForNewContract(quoteRequest)
    } catch (e: ExternalServiceBadRequestException) {
      logger.error("Failed to create quote", e)
      null
    }
  }

  override fun updateQuote(
    quoteId: UUID,
    quoteDto: QuoteInputDto,
    underwritingGuidelinesBypassedBy: String?
  ): QuoteDto {
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
    previousAgreementActiveTo: LocalDate?
  ): QuoteDto {
    logger.info("Adding agreement from quote=$quoteId")
    val addedQuote = underwriterClient.addAgreementFromQuote(
      AddAgreementFromQuoteRequest(
        quoteId = quoteId,
        contractId = contractId,
        activeFrom = activeFrom,
        activeTo = activeTo,
        previousAgreementActiveTo = previousAgreementActiveTo
      )
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

  override fun createQuoteForNewContract(request: QuoteForNewContractRequestDto): QuoteResponseDto {
    return underwriterCreateQuoteForNewContract(request.copy(underwritingGuidelinesBypassedBy = null))
      ?: underwriterCreateQuoteForNewContract(request)
      ?: throw RuntimeException("Could not create quote for new contract from member ${request.quoteRequestDto.memberId}")
  }

  override fun signQuoteForNewContract(
    completeQuoteId: UUID,
    request: SignQuoteFromHopeRequestDto
  ): SignedQuoteResponseDto {
    try {
      val response = underwriterClient.signQuoteForNewContract(completeQuoteId, request)
      if (response.statusCode.is2xxSuccessful) {
        return response.body as SignedQuoteResponseDto
      }
    } catch (ex: RestClientResponseException) {
      if (ex.rawStatusCode == 422){
        logger.error("Cannot sign quote [QuoteId: $completeQuoteId] [ResposneBody: ${ex.responseBodyAsString}] [Exception: $ex]")
      }
      throw ex
    }
    throw IllegalStateException("Cannot sign quote [QuoteId: $completeQuoteId]")
  }
}
