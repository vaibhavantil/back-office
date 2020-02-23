package com.hedvig.backoffice.graphql.types;

import com.hedvig.backoffice.services.product_pricing.dto.SwitchableSwitcherCompany;
import com.hedvig.backoffice.services.product_pricing.dto.SwitchableSwitcherEmailDTO;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.UUID;

public class SwitchableSwitcherEmail {
  private UUID id;
  private String memberId;
  private SwitchableSwitcherCompany switcherCompany;
  private Instant queuedAt;
  @Nullable
  private Instant sentAt;
  @Nullable
  private Instant remindedAt;

  public SwitchableSwitcherEmail() {
  }

  public SwitchableSwitcherEmail(
    final UUID id,
    final String memberId,
    final SwitchableSwitcherCompany switcherCompany,
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

  public SwitchableSwitcherCompany getSwitcherCompany() {
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

  public static SwitchableSwitcherEmail from(final SwitchableSwitcherEmailDTO switchableSwitcherEmailDTO) {
    return new SwitchableSwitcherEmail(
      switchableSwitcherEmailDTO.getId(),
      switchableSwitcherEmailDTO.getMemberId(),
      switchableSwitcherEmailDTO.getSwitcherCompany(),
      switchableSwitcherEmailDTO.getQueuedAt(),
      switchableSwitcherEmailDTO.getSentAt(),
      switchableSwitcherEmailDTO.getRemindedAt()
    );
  }
}
