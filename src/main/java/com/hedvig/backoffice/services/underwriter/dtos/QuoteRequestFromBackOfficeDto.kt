package com.hedvig.backoffice.services.underwriter.dtos

import java.util.*

data class QuoteRequestFromBackOfficeDto(
  val agreementId: UUID,
  val memberId: String,
  val underwritingGuidelinesBypassedBy: String?
)
