package com.hedvig.backoffice.services.itemizer.dto.request

import java.util.*

data class UpsertItemTypeRequest(
  val id: UUID?,
  val name: String,
  val itemFamilyId: String
)
