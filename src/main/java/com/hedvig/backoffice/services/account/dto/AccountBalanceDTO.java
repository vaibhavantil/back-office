package com.hedvig.backoffice.services.account.dto;

import lombok.Value;

import javax.money.MonetaryAmount;

@Value
public class AccountBalanceDTO {
  private String memberId;
  private MonetaryAmount currentBalance;
  private MonetaryAmount totalBalance;
}
