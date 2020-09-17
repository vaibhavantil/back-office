package com.hedvig.backoffice.graphql.types

data class MemberSearchResult(
  val members: List<Member>,
  val totalPages: Int,
  val page: Int
)
