package com.hedvig.backoffice.services.payments.dto;

import java.time.Instant;
import java.util.UUID;
import javax.money.MonetaryAmount;
import lombok.Value;
import org.javamoney.moneta.Money;

@Value
public class Transaction {
  UUID id;
  MonetaryAmount amount;
  Instant timestamp;
  String type;
  String status;

  public static Transaction fromTransactionDTO(UUID id, TransactionDTO dto) {
    return new Transaction(
        id,
        Money.of(dto.getAmount(), dto.getCurrency()),
        dto.getTimestamp(),
        dto.getTransactionType(),
        dto.getTransactionStatus());
  }
}
