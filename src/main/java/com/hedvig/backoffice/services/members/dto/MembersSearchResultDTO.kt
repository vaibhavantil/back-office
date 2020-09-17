package com.hedvig.backoffice.services.members.dto

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@AllArgsConstructor
@NoArgsConstructor
data class MembersSearchResultDTO (
  val members: List<MemberDTO>,
  val page: Int,
  val totalPages: Int
)
