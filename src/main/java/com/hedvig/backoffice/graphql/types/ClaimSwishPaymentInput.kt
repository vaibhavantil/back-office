package com.hedvig.backoffice.graphql.types

import javax.money.MonetaryAmount

data class ClaimSwishPaymentInput(
    val amount: MonetaryAmount,
    val deductible: MonetaryAmount,
    val note: String,
    val exGratia: Boolean,
    val sanctionListSkipped: Boolean,
    val phoneNumber: String,
    val message: String,
    val carrier: String
)
