package com.hedvig.backoffice.services.members.dto

import java.time.LocalDate

class MemberSearchResultItemDTO(
  val member: MemberDTO,
  var firstActiveFrom: LocalDate? = null,
  var lastActiveTo: LocalDate? = null,
  var productStatus: String? = null
)
