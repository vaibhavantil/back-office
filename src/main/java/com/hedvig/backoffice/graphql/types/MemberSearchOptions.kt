package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.members.dto.MembersSortColumn
import org.springframework.data.domain.Sort

data class MemberSearchOptions(
  val includeAll: Boolean?,
  val page: Int?,
  val pageSize: Int?,
  val sortBy: MembersSortColumn?,
  val sortDirection: Sort.Direction?
)
