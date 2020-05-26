package com.hedvig.backoffice.graphql.types

import java.time.LocalDate

data class CampaignFilter (
  val code: String,
  val partnerId: String,
  val activeFrom: LocalDate,
  val activeTo: LocalDate
)
