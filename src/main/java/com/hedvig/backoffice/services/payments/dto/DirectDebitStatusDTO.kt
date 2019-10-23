package com.hedvig.backoffice.services.payments.dto

data class DirectDebitStatusDTO(
    val memberId: String,
    val directDebitActivated: Boolean = false
)
