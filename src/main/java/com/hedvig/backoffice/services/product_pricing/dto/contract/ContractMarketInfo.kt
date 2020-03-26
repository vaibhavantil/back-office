package com.hedvig.backoffice.services.product_pricing.dto.contract

import javax.money.CurrencyUnit

data class ContractMarketInfo(
  val market: Market,
  val preferredCurrency: CurrencyUnit
)
