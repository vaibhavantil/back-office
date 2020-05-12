package com.hedvig.backoffice.services.underwriter.dtos

import java.time.Instant
import java.util.UUID

class SignedQuoteResponseDto(
  val id: UUID,
  val signedAt: Instant
)
