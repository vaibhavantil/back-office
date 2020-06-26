package com.hedvig.backoffice.services.itemizer.dto

import java.math.BigDecimal

data class Evaluation (
  val depreciatedValue: BigDecimal?,
  val evaluationRule: EvaluationRule?
)
