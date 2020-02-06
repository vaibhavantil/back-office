package com.hedvig.backoffice.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Personnel {
  @Id private String id;

  private String email;

  public Personnel(String id) {
    this.id = id;
  }
}
