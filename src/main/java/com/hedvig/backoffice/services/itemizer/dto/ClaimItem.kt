package com.hedvig.backoffice.services.itemizer.dto

import com.hedvig.graphql.commons.type.MonetaryAmountV2
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
  val purchasePrice: MonetaryAmount?,
  val dateOfLoss: LocalDate,
  val note: String?
) {
  val purchasePriceAmount: MonetaryAmountV2?
    get() = if (purchasePrice != null) MonetaryAmountV2.of(purchasePrice) else null
}
