package com.hedvig.backoffice.services.claims.dto

import javax.money.MonetaryAmount

data class ClaimSwishPayment(
    val amount: MonetaryAmount,
    val deductible: MonetaryAmount,
    val note: String,
    val exGratia: Boolean,
    val sanctionListSkipped: Boolean,
    val ssn: String,
    val phoneNumber: String,
    val message: String
)
