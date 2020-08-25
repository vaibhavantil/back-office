package com.hedvig.backoffice.graphql.types.claims

data class ClaimInfo(
  val numberOfClaims: Int,
  val hasOpenClaim: Boolean
)
