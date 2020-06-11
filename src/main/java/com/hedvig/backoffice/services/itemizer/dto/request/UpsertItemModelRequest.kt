package com.hedvig.backoffice.services.itemizer.dto.request

import java.util.*

data class UpsertItemModelRequest(
  val id: UUID?,
  val name: String,
  val itemBrandId: UUID
)
