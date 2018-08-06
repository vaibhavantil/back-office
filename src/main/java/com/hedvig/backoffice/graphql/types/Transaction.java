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
}
