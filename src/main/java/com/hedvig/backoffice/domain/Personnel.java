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

  private String name;

  private String picture;

  @Lob
  @Type(type = "org.hibernate.type.TextType")
  private String idToken;

  private String refreshToken;

  public Personnel(String id) {
    this.id = id;
  }
}
