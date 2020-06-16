package com.hedvig.backoffice.services.underwriter.dtos

import java.util.UUID

data class QuoteFromAgreementRequestDto(
  val agreementId: UUID,
  val memberId: String,
  val underwritingGuidelinesBypassedBy: String?
)
