package com.hedvig.backoffice.graphql.types.account;

import com.hedvig.backoffice.services.account.dto.AccountEntryDTO;
import com.hedvig.backoffice.services.account.dto.AccountEntryType;
import lombok.Value;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Value
public class AccountEntry {
  UUID id;
  LocalDate fromDate;
  MonetaryAmount amount;
  AccountEntryType type;
  String source;
  String reference;
  Optional<String> title;
  Optional<String> comment;

  public static AccountEntry from(AccountEntryDTO accountEntryDTO) {
    return new AccountEntry(
      accountEntryDTO.getId(),
      accountEntryDTO.getFromDate(),
      accountEntryDTO.getAmount(),
      accountEntryDTO.getType(),
      accountEntryDTO.getSource(),
      accountEntryDTO.getReference(),
      accountEntryDTO.getTitle(),
      accountEntryDTO.getComment()
    );
  }
}
