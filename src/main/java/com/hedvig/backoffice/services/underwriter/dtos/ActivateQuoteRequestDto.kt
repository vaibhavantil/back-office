package com.hedvig.backoffice.services.underwriter.dtos

import java.time.LocalDate

data class ActivateQuoteRequestDto(
  val activationDate: LocalDate?,
  val terminationDate: LocalDate?
)
