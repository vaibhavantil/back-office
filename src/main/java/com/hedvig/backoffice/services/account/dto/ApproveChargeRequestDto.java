package com.hedvig.backoffice.services.account.dto;

import com.hedvig.backoffice.graphql.types.MemberChargeApproval;
import lombok.Value;

import javax.money.MonetaryAmount;
import java.util.List;

@Value
public class ApproveChargeRequestDto {
  String memberId;
  MonetaryAmount amount;

  public static ApproveChargeRequestDto from(MemberChargeApproval memberChargeApproval) {
    return new ApproveChargeRequestDto(
      memberChargeApproval.getMemberId(),
      memberChargeApproval.getAmount()
    );
  }
}
