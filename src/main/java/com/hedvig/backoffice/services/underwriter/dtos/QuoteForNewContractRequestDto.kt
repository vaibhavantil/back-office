package com.hedvig.backoffice.services.underwriter.dtos

data class QuoteForNewContractRequestDto(
  val quoteRequestDto: QuoteRequestDto,
  val underwritingGuidelinesBypassedBy: String?
)
