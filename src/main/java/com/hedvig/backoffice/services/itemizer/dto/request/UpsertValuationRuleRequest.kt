package com.hedvig.backoffice.services.itemizer.dto.request

import com.hedvig.backoffice.services.product_pricing.dto.contract.TypeOfContract
import java.math.BigDecimal
import java.util.*

data class UpsertValuationRuleRequest(
  val id: UUID? = null,
  val name: String,
  val ageLimit: BigDecimal,
  val typeOfContract: TypeOfContract,
  val itemFamilyId: String,
  val itemTypeId: UUID?,
  val valuationType: String,
  val depreciation: Int?
)
