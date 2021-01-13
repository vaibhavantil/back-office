package com.hedvig.backoffice.services.members.dto

import com.hedvig.backoffice.graphql.resolvers.NationalIdentification

data class NationalIdentificationDto(
    val identification: String,
    val nationality: String
) {
    fun toGraphQLType() = NationalIdentification(
        identification,
        nationality
    )
}
