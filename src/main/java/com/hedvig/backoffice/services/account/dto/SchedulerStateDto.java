package com.hedvig.backoffice.services.account.dto;

import com.hedvig.backoffice.services.account.ChargeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SchedulerStateDto {
  UUID stateId;
  String memberId;
  ChargeStatus chargeStatus;
  String changedBy;
  Instant changedAt;
  Optional<MonetaryAmount> amount;
  Optional<UUID> transactionId;
}
