package com.hedvig.backoffice.services.members.dto

data class MembersSearchResultDTO(
  val members: List<MemberDTO>,
  val page: Int,
  val totalPages: Int
)
