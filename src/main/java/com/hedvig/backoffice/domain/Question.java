package com.hedvig.backoffice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Question {

    @Id
    private Long id;

    @NotNull
    @Lob
    @Type(type="org.hibernate.type.TextType")
    private String message;

    @Lob
    @Type(type="org.hibernate.type.TextType")
    private String answer;

    @ManyToOne
    private Personnel personnel;

    private Instant date;

    private Instant answerDate;

    @ManyToOne
    private Subscription subscription;

    public Question(long id, String message, Instant date) {
        this.id = id;
        this.message = message;
        this.date = date;
    }

}
