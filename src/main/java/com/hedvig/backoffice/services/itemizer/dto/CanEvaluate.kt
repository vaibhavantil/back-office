package com.hedvig.backoffice.services.itemizer.dto

import java.util.*

data class CanEvaluate (
  val canEvaluate: Boolean,
  val itemFamily: String?,
  val itemTypeId: UUID?
)
