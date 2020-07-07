package com.hedvig.backoffice.services.itemizer.dto

import com.hedvig.graphql.commons.type.MonetaryAmountV2
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.money.MonetaryAmount

data class ClaimItem(
  val id: UUID,
  val itemFamily: ItemFamily,
  val itemType: ItemType,
  val itemBrand: ItemBrand?,
  val itemModel: ItemModel?,
  val itemCompany: ItemCompany?,
  val dateOfPurchase: LocalDate?,
  val purchasePrice: MonetaryAmountV2?,
  val valuation: MonetaryAmountV2?,
  val note: String?
)
