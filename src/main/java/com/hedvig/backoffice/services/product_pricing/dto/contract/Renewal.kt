package com.hedvig.backoffice.services.product_pricing.dto.contract

import java.time.LocalDate
import java.util.*

data class Renewal(
  val renewalDate: LocalDate,
  val draftCertificateUrl: String?,
  val draftOfAgreementId: UUID?
)
