package com.hedvig.backoffice.services.payments.dto

import javax.money.MonetaryAmount

class PayoutMemberRequest(
    val amount: MonetaryAmount,
    val sanctionBypassed: Boolean?,
    val category: PayoutCategory?,
    val referenceId: String?,
    val note: String?,
    val carrier: String?,
    val payoutDetails: SelectedPayoutDetails?
)
