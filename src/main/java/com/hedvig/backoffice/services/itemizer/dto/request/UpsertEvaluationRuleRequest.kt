package com.hedvig.itemizer.evaluations.web.dto.request

import java.math.BigDecimal
import java.util.*

data class UpsertEvaluationRuleRequest(
  val id: UUID? = null,
  val name: String,
  val ageLimit: BigDecimal,
  val typeOfContract: String,
  val itemFamilyId: String,
  val itemTypeId: UUID?,
  val evaluationType: String,
  val depreciation: BigDecimal?
)
