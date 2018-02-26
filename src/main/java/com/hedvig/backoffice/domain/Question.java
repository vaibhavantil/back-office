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

    @NotNull
    private String hid;

    private Instant timestamp;

    public Question(String hid, String message) {
        this.hid = hid;
        this.message = message;
    }

}
