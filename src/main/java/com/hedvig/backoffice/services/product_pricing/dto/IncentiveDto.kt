package com.hedvig.backoffice.services.product_pricing.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.hedvig.backoffice.graphql.UnionType
import java.math.BigDecimal
import javax.money.MonetaryAmount

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(value = MonthlyPercentageDiscountFixedPeriod::class, name = "MonthlyPercentageDiscountFixedPeriod"),
  JsonSubTypes.Type(value = FreeMonths::class, name = "FreeMonths"),
  JsonSubTypes.Type(value = CostDeduction::class, name = "CostDeduction"),
  JsonSubTypes.Type(value = NoDiscount::class, name = "NoDiscount"),
  JsonSubTypes.Type(value = IndefinitePercentageDiscount::class, name = "IndefinitePercentageDiscount"),
  JsonSubTypes.Type(value = VisibleNoDiscount::class, name = "VisibleNoDiscount")
)
@UnionType
sealed class IncentiveDto

@UnionType
data class MonthlyPercentageDiscountFixedPeriod(
  val numberOfMonths: Int,
  val percentage: BigDecimal
): IncentiveDto()

@UnionType
data class FreeMonths(
  val numberOfMonths: Int
): IncentiveDto()

@UnionType
data class CostDeduction(
  val amount: MonetaryAmount
): IncentiveDto()

@UnionType
data class NoDiscount(
  val `_`: Boolean = true
): IncentiveDto()

@UnionType
data class IndefinitePercentageDiscount(
  val percentageDiscount: BigDecimal
): IncentiveDto()

@UnionType
data class VisibleNoDiscount(
  val `_`: Boolean = true
): IncentiveDto()
