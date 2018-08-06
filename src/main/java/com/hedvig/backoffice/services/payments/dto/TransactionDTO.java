package com.hedvig.backoffice.services.payments.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Value;

@Value
public class TransactionDTO {
  UUID id;
  BigDecimal amount;
  String currency;
  Instant timestamp;
  String transactionType;
  String transactionStatus;
}
