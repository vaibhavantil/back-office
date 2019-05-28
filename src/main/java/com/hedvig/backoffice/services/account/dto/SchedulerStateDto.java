package com.hedvig.backoffice.services.account.dto;

import com.hedvig.backoffice.services.account.ChargeStatus;

import lombok.Value;

import javax.money.MonetaryAmount;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Value
public class SchedulerStateDto {
  UUID stateId;
  String memberId;
  ChargeStatus status;
  String changedBy;
  Instant changedAt;
  Optional<MonetaryAmount> amount;
  Optional<UUID> transactionId;
}
