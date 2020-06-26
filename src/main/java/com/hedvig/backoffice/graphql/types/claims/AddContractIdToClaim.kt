package com.hedvig.backoffice.graphql.types.claims

import java.util.*

data class AddContractIdToClaim(
  val memberId: String,
  val claimId: UUID,
  val contractId: UUID
)
