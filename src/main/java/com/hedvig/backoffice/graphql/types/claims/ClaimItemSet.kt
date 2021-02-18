package com.hedvig.backoffice.graphql.types.claims

import com.hedvig.backoffice.services.itemizer.dto.ClaimItem

data class ClaimItemSet(
    val items: List<ClaimItem>
)
