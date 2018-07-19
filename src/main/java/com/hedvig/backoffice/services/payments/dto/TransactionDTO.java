package com.hedvig.backoffice.services.payments.dto;

import java.time.Instant;

import javax.money.MonetaryAmount;

import lombok.Value;

@Value
public class TransactionDTO {
    MonetaryAmount amount;
    Instant timestamp;
    String type;
    String status;
}
