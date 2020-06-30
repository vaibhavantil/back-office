package com.hedvig.backoffice.graphql.types.claims

data class AddContractIdToClaim(
  val claimId: String,
  val memberId: String,
  val contractId: String
)
