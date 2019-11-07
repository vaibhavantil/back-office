package com.hedvig.backoffice.services.underwriter

import com.hedvig.backoffice.services.members.MemberService
import com.hedvig.backoffice.services.underwriter.dtos.ActivateQuoteRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.IncompleteApartmentQuoteDataDto
import com.hedvig.backoffice.services.underwriter.dtos.IncompleteHouseQuoteDataDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteResponseDto
import com.hedvig.backoffice.web.dto.CreateQuoteFromProductDto
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.UUID

@Component // TODO make proper impl
class UnderwriterServiceImpl(
  private val underwriterClient: UnderwriterClient,
  private val memberService: MemberService
) : UnderwriterService {
  override fun createAndCompleteQuote(memberId: String, quoteDto: CreateQuoteFromProductDto): QuoteResponseDto {
    val member = memberService.findByMemberId(memberId, "")
    val createdQuote = underwriterClient.createQuote(
      QuoteRequestDto(
        firstName = member.firstName!!,
        lastName = member.lastName!!,
        ssn = member.ssn,
        memberId = memberId,
        originatingProductId = quoteDto.originatingProductId,
        currentInsurer = quoteDto.currentInsurer,
        birthDate = member.birthDate,
        incompleteApartmentQuoteData = quoteDto.incompleteApartmentQuoteData?.let((IncompleteApartmentQuoteDataDto)::from),
        incompleteHouseQuoteData = quoteDto.incompleteHouseQuoteData?.let((IncompleteHouseQuoteDataDto)::from),
        quotingPartner = null
      )
    )

    underwriterClient.completeQuote(createdQuote.id)
    return createdQuote
  }

  override fun activateQuote(quoteId: UUID, activationDate: LocalDate?, terminationDate: LocalDate?): QuoteDto =
    underwriterClient.activateQuote(quoteId, ActivateQuoteRequestDto(activationDate, terminationDate))

  override fun getQuotes(memberId: String): List<QuoteDto> =
    underwriterClient.getQuotes(memberId)
}
