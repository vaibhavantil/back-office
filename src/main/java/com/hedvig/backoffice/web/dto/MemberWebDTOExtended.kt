package com.hedvig.backoffice.web.dto

import java.time.LocalDate

data class MemberWebDTOExtended(
  val member: MemberWebDTO,
  var firstActiveFrom: LocalDate?,
  var lastActiveTo: LocalDate?,
  var productStatus: String?,
  var householdSize: Int?,
  var livingSpace: Int?
)
