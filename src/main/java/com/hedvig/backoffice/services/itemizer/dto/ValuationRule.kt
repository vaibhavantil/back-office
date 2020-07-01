package com.hedvig.backoffice.services.itemizer.dto

import java.math.BigDecimal
import java.util.*

data class ValuationRule (
  val valuationName: String,
  val itemFamily: String,
  val itemTypeId: UUID?,
  val ageLimit: BigDecimal,
  val typeOfContract: String,
  val valuationType: String,
  val depreciation: Int?
)
