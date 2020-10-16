package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.members.dto.MembersSearchResultDTO

data class MemberSearchResult(
  val members: List<Member>,
  val totalPages: Int,
  val page: Int
) {
  companion object {
    fun from(membersSearchResultDTO: MembersSearchResultDTO) = MemberSearchResult(
      members = membersSearchResultDTO.members.map((Member)::fromDTO),
      totalPages = membersSearchResultDTO.totalPages,
      page = membersSearchResultDTO.page
    )
  }
}
