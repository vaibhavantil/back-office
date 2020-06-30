package com.hedvig.backoffice.graphql.types.claims

data class SetContractForClaim(
  val claimId: String,
  val memberId: String,
  val contractId: String
)
