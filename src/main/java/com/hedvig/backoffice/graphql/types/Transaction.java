package com.hedvig.backoffice.graphql.types;

import java.time.Instant;

import javax.money.MonetaryAmount;

import lombok.Value;

@Value
public class Transaction {
    MonetaryAmount amount;
    Instant timestamp;
    String type;
    String status;
}
