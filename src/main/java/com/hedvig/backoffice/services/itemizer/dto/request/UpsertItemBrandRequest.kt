package com.hedvig.backoffice.services.itemizer.dto.request

import java.util.*

data class UpsertItemBrandRequest(
  val id: UUID?,
  val name: String,
  val itemTypeId: UUID,
  val itemCompanyId: UUID
)
