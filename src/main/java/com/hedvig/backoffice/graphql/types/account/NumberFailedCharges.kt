package com.hedvig.backoffice.graphql.types.account

import com.hedvig.backoffice.services.account.dto.NumberFailedChargesDto
import java.time.Instant

data class NumberFailedCharges(
    val numberFailedCharges: Int,
    val lastFailedChargeAt: Instant?
) {
    companion object {
        fun from(numberFailedCharges: NumberFailedChargesDto) = NumberFailedCharges(
            numberFailedCharges = numberFailedCharges.numberFailedCharges,
            lastFailedChargeAt = numberFailedCharges.lastFailedChargeAt
        )
    }
}
