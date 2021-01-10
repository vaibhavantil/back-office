package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.claims.dto.ClaimSearchResultDTO

data class ListClaimsResult(
    val claims: List<Claim>,
    val totalPages: Int,
    val page: Int
) {
    companion object {
        fun from(listClaimsSearchResultDTO: ClaimSearchResultDTO) = ListClaimsResult(
            claims = listClaimsSearchResultDTO.claims.map(Claim::fromDTO),
            totalPages = listClaimsSearchResultDTO.totalPages,
            page = listClaimsSearchResultDTO.page
        )
    }
}
