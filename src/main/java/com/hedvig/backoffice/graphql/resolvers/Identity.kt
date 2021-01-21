package com.hedvig.backoffice.graphql.resolvers

data class Identity(
    val nationalIdentification: NationalIdentification,
    val firstName: String?,
    val lastName: String?
)
