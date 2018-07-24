package com.hedvig.backoffice.services.payments.dto;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Value;

@Value
public class TransactionDTO {
    BigDecimal amount;
    String currency;
    Instant timestamp;
    String type;
    String status;
}
