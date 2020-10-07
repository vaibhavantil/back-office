package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.claims.dto.ClaimSearchResultDTO

data class ClaimSearchResult(
  val claims: List<Claim>,
  val totalPages: Int,
  val page: Int
) {
  companion object {
    fun from(claimSearchResultDTO: ClaimSearchResultDTO) = ClaimSearchResult(
      claims = claimSearchResultDTO.claims.map((Claim)::fromDTO),
      totalPages = claimSearchResultDTO.totalPages,
      page = claimSearchResultDTO.page
    )
  }
}
