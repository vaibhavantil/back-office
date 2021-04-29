package com.hedvig.backoffice.graphql.types

import javax.money.MonetaryAmount

class MemberChargeApproval(
    val memberId: String,
    val amount: MonetaryAmount
)
