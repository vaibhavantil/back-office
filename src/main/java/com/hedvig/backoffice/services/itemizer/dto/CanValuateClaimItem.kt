package com.hedvig.backoffice.services.itemizer.dto

import com.hedvig.backoffice.services.product_pricing.dto.contract.TypeOfContract
import java.util.*

data class CanValuateClaimItem(
  val canValuate: Boolean,
  val typeOfContract: TypeOfContract?,
  val itemFamily: String?,
  val itemTypeId: UUID?
)
