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
  val purchasePriceAmount: BigDecimal?,
  val purchasePriceCurrency: String?,
  val note: String?
) {
  val purchasePrice: MonetaryAmountV2?
    get() = if (purchasePriceAmount != null && purchasePriceCurrency != null)
      MonetaryAmountV2.of(purchasePriceAmount, purchasePriceCurrency) else null
}
