package com.hedvig.backoffice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue
    private Long Id;

    @NotNull
    @Lob
    private String message;

    @Lob
    private String answer;

    @ManyToOne
    private Personnel personnel;

    private Instant date;

    private Instant answerDate;

    @ManyToOne
    private Subscription subscription;

    public Question(String message, Instant date) {
        this.message = message;
        this.date = date;
    }

}
