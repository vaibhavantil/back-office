package com.hedvig.backoffice.graphql.types;

import java.time.Instant;
import java.util.UUID;
import javax.money.MonetaryAmount;
import lombok.Value;

@Value
public class Transaction {
  UUID id;
  MonetaryAmount amount;
  Instant timestamp;
  String type;
  String status;

  public static Transaction fromDTO(com.hedvig.backoffice.services.payments.dto.Transaction dto) {
    return new Transaction(dto.getId(), dto.getAmount(), dto.getTimestamp(), dto.getType(),
        dto.getStatus());
  }
}
