package com.hedvig.backoffice.services.account.dto;

import lombok.Value;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Value
public class AccountEntryDTO {
  UUID id;
  LocalDate fromDate;
  MonetaryAmount amount;
  AccountEntryType type;
  String source;
  String reference;
  Optional<String> title;
  Optional<String> comment;
}
