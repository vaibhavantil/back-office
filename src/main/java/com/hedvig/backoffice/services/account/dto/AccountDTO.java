package com.hedvig.backoffice.services.account.dto;

import lombok.Value;

import javax.money.MonetaryAmount;
import java.util.List;

@Value
public class AccountDTO {
  String id;
  MonetaryAmount balance;
  List<AccountEntryDTO> entries;
}
