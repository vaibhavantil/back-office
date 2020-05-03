package com.hedvig.backoffice.services.itemizer.dto.request

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID
import javax.money.MonetaryAmount

data class UpsertClaimItemRequest(
    val id: UUID?,
    val claimId: UUID,
    val itemFamilyId: String,
    val itemTypeId: UUID,
    val itemBrandId: UUID?,
    val itemModelId: UUID?,
    val dateOfPurchase: LocalDate?,
    val purchasePriceAmount: BigDecimal?,
    val purchasePriceCurrency: String?,
    val dateOfLoss: LocalDate,
    val note: String?
)
