package com.hedvig.backoffice.graphql.types;

import com.hedvig.backoffice.services.account.ChargeStatus;
import lombok.Value;


import javax.money.MonetaryAmount;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Value
public class SchedulerStatus {
  UUID id;
  String memberId;
  ChargeStatus chargeStatus;
  String changedBy;
  Instant changedAt;
  Optional<MonetaryAmount> amount;
  Optional<UUID> transactionId;
}

