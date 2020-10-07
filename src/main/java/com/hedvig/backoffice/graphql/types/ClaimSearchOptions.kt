package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.claims.dto.ClaimSortColumn
import org.springframework.data.domain.Sort

data class ClaimSearchOptions(
  val page: Int?,
  val pageSize: Int?,
  val sortBy: ClaimSortColumn = ClaimSortColumn.DATE,
  val sortDirection: Sort.Direction = Sort.Direction.DESC
)
