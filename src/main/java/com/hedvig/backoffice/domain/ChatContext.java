package com.hedvig.backoffice.domain;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatContext {

  @Id @GeneratedValue private Long id;

  @NotNull private String memberId;

  @NotNull private String subId;

  @NotNull private String sessionId;

  @NotNull private Instant timestamp;

  @NotNull
  @Column(columnDefinition = "boolean default false", nullable = false)
  private boolean active = false;

  @ManyToOne private Subscription subscription;

  @ManyToOne private Personnel personnel;
}
