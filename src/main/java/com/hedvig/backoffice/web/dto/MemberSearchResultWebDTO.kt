package com.hedvig.backoffice.web.dto

data class MemberSearchResultWebDTO(
  val items: List<MemberWebDTOExtended>,
  val page: Int?,
  val totalPages: Int?
)
