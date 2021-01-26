package com.hedvig.backoffice.services.payments.dto

data class PayoutMethodStatusDTO(
    val memberId: String,
    val activated: Boolean
)
