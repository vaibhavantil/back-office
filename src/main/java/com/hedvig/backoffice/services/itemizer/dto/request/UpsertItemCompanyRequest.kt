package com.hedvig.backoffice.services.itemizer.dto.request

import java.util.*

data class UpsertItemCompanyRequest(
  val id: UUID?,
  val name: String
)
