package com.hedvig.backoffice.graphql.types;

import lombok.Value;

import javax.money.MonetaryAmount;

@Value
public class MemberChargeApproval {
  String memberId;
  MonetaryAmount amount;
}
