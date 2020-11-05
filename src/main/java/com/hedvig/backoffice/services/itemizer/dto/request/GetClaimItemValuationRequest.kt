package com.hedvig.backoffice.services.itemizer.dto.request

import com.hedvig.backoffice.services.product_pricing.dto.contract.TypeOfContract
import java.time.LocalDate
import java.util.*
import javax.money.MonetaryAmount

data class GetClaimItemValuationRequest(
  val purchasePrice: MonetaryAmount,
  val itemFamilyId: String,
  val itemTypeId: UUID?,
  val typeOfContract: TypeOfContract?,
  val purchaseDate: LocalDate,
  val baseDate: LocalDate?
)
