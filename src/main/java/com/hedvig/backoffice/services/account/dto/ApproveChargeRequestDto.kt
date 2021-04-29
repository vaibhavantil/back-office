package com.hedvig.backoffice.services.account.dto

import com.hedvig.backoffice.graphql.types.MemberChargeApproval
import lombok.Value
import javax.money.MonetaryAmount

@Value
class ApproveChargeRequestDto(
    val memberId: String,
    val amount: MonetaryAmount
) {
    companion object {
        fun from(memberChargeApproval: MemberChargeApproval): ApproveChargeRequestDto {
            return ApproveChargeRequestDto(
                memberChargeApproval.memberId,
                memberChargeApproval.amount
            )
        }
    }
}
