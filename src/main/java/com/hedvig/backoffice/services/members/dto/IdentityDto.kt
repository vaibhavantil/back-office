package com.hedvig.backoffice.services.members.dto

import com.hedvig.backoffice.graphql.resolvers.Identity

data class IdentityDto(
    val nationalIdentification: NationalIdentificationDto,
    val firstName: String?,
    val lastName: String?
) {
    fun toGraphQLType() = Identity(
        nationalIdentification.toGraphQLType(),
        firstName,
        lastName
    )
}
