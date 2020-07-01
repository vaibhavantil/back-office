package com.hedvig.backoffice.services.itemizer.dto

import java.util.*

data class CanValuateClaimItem(
  val canValuate: Boolean,
  val itemFamily: String?,
  val itemTypeId: UUID?
)
