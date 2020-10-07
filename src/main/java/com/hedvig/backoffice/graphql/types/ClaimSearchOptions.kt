package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.claims.dto.ClaimSortColumn
import org.springframework.data.domain.Sort

data class ClaimSearchOptions(
  val page: Int? = 0,
  val pageSize: Int? = 20,
  val sortBy: ClaimSortColumn = ClaimSortColumn.DATE,
  val sortDirection: Sort.Direction = Sort.Direction.DESC
)
