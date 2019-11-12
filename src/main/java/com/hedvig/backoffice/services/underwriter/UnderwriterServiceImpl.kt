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
import java.time.LocalDate
import java.util.UUID

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

    underwriterClient.completeQuote(createdQuote.id)
    return createdQuote
  }

  override fun updateQuote(quoteId: UUID, quoteDto: QuoteInputDto): QuoteDto =
    underwriterClient.updateQuote(quoteId, quoteDto)

  override fun activateQuote(quoteId: UUID, activationDate: LocalDate?, terminationDate: LocalDate?): QuoteDto =
    underwriterClient.activateQuote(quoteId, ActivateQuoteRequestDto(activationDate, terminationDate))

  override fun getQuotes(memberId: String): List<QuoteDto> =
    underwriterClient.getQuotes(memberId)
}
