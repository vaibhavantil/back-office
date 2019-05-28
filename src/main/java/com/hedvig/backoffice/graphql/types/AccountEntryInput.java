package com.hedvig.backoffice.graphql.types;

import com.hedvig.backoffice.services.account.dto.AccountEntryType;
import lombok.Value;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.Optional;

@Value
public class AccountEntryInput {
  AccountEntryType type;
  MonetaryAmount amount;
  LocalDate fromDate;
  String reference;
  String source;
  Optional<String> title;
  Optional<String> comment;
}
