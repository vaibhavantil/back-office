package com.hedvig.backoffice.services.underwriter.dtos

import java.time.LocalDate
import java.util.*

data class AddAgreementFromQuoteRequest(
    val quoteId: UUID,
    val contractId: UUID?,
    val activeFrom: LocalDate?,
    val activeTo: LocalDate?,
    val previousAgreementActiveTo: LocalDate?
)
