package com.hedvig.backoffice.services.account.dto;

import lombok.Value;

import javax.money.MonetaryAmount;
import java.util.List;

@Value
public class AccountDTO {
  String memberId;
  MonetaryAmount currentMonthsBalance;
  MonetaryAmount totalBalance;
  List<AccountEntryDTO> entries;
}
