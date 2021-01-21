package com.hedvig.backoffice.services.claims.dto


data class ClaimSearchResultDTO (
    val claims: List<Claim>,
    val page: Int,
    val totalPages: Int
)
