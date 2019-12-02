package com.hedvig.backoffice.web.dto

import java.time.LocalDate

data class MemberWebDTOExtended(
  val member: MemberWebDTO,
  var firstActiveFrom: LocalDate? = null,
  var lastActiveTo: LocalDate? = null,
  var productStatus: String? = null,
  var householdSize: Int?,
  var livingSpace: Int?
)
