package com.hedvig.backoffice.services.itemizer.dto

import java.math.BigDecimal
import java.util.*

data class EvaluationRule (
  val evaluationName: String,
  val itemFamily: String,
  val itemTypeId: UUID?,
  val ageLimit: BigDecimal,
  val typeOfContract: String,
  val evaluationType: String,
  val depreciation: BigDecimal?
)
