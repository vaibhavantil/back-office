package com.hedvig.backoffice.services.account.dto;

import lombok.Value;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Value
public class Entry {
  UUID id;
  LocalDate fromDate;
  MonetaryAmount amount;
  EntryType type;
  String source;
  String reference;
  Optional<String> title;
  Optional<String> comment;
}
