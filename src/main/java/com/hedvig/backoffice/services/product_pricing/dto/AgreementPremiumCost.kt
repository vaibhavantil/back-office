package com.hedvig.backoffice.services.product_pricing.dto

import java.util.*
import javax.money.MonetaryAmount

data class AgreementPremiumCost(
    val agreementId: UUID,
    val lineOfBusinessName: String,
    val cost: MonetaryAmount
)
