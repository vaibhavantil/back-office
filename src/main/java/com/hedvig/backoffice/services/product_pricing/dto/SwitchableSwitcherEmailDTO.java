package com.hedvig.backoffice.services.product_pricing.dto;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.UUID;

public class SwitchableSwitcherEmailDTO {
  private UUID id;
  private String memberId;
  private String switcherCompany;
  private Instant queuedAt;
  @Nullable
  private Instant sentAt;
  @Nullable
  private Instant remindedAt;

  public SwitchableSwitcherEmailDTO() {
  }

  public SwitchableSwitcherEmailDTO(
    final UUID id,
    final String memberId,
    final String switcherCompany,
    final Instant queuedAt,
    final @Nullable Instant sentAt,
    final @Nullable Instant remindedAt
  ) {
    this.id = id;
    this.memberId = memberId;
    this.switcherCompany = switcherCompany;
    this.queuedAt = queuedAt;
    this.sentAt = sentAt;
    this.remindedAt = remindedAt;
  }

  public UUID getId() {
    return id;
  }

  public String getMemberId() {
    return memberId;
  }

  public String getSwitcherCompany() {
    return switcherCompany;
  }

  public Instant getQueuedAt() {
    return queuedAt;
  }

  @Nullable
  public Instant getSentAt() {
    return sentAt;
  }

  @Nullable
  public Instant getRemindedAt() {
    return remindedAt;
  }
}
