package com.hedvig.backoffice.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UpdateContext {

  @Id @GeneratedValue private Long id;

  private String sessionId;

  @NotNull @OneToOne private Personnel personnel;

  public UpdateContext(Personnel personnel, String sessionId) {
    this.personnel = personnel;
    this.sessionId = sessionId;
  }
}
