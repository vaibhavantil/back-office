package com.hedvig.backoffice.services.account.dto;

import com.hedvig.backoffice.graphql.types.account.AccountEntryInput;
import lombok.Value;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Value
public class AccountEntryRequestDTO {
  LocalDate fromDate;
  MonetaryAmount amount;
  AccountEntryType type;
  String source;
  String reference;
  Optional<UUID> matchesEntryId;
  Optional<String> title;
  Optional<String> comment;
  String addedBy;

  public static AccountEntryRequestDTO from(final AccountEntryInput accountEntryInput, final String addedBy) {
    return new AccountEntryRequestDTO(
      accountEntryInput.getFromDate(),
      accountEntryInput.getAmount(),
      accountEntryInput.getType(),
      accountEntryInput.getSource(),
      accountEntryInput.getReference(),
      Optional.empty(),
      accountEntryInput.getTitle(),
      accountEntryInput.getComment(),
      addedBy
    );
  }
}
