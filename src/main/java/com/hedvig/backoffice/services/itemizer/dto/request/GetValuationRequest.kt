package com.hedvig.backoffice.services.itemizer.dto.request

import java.time.LocalDate
import java.util.*
import javax.money.MonetaryAmount

data class GetValuationRequest(
  val purchasePrice: MonetaryAmount,
  val itemFamilyId: String,
  val itemTypeId: UUID?,
  val typeOfContract: String,
  val purchaseDate: LocalDate,
  val baseDate: LocalDate?
)
