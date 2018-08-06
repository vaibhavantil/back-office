package com.hedvig.backoffice.domain;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Question {

  @Id private Long id;

  @NotNull
  @Lob
  @Type(type = "org.hibernate.type.TextType")
  private String message;

  private Instant date;

  @ManyToOne private QuestionGroup group;

  public Question(long id, String message, Instant date) {
    this.id = id;
    this.message = message;
    this.date = date;
  }
}
