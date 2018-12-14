package com.hedvig.backoffice.services.payments.dto;

import java.time.Instant;
import java.util.UUID;
import javax.money.MonetaryAmount;
import lombok.Value;

@Value
public class TransactionDTO {
  UUID id;
  MonetaryAmount amount;
  Instant timestamp;
  String transactionType;
  String transactionStatus;
}
