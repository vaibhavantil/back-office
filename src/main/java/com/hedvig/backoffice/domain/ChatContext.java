package com.hedvig.backoffice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatContext {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String hid;

    @NotNull
    private String subId;

    @NotNull
    private String sessionId;

    @NotNull
    private Instant timestamp;

    @NotNull
    private boolean active;

    @ManyToOne
    private Subscription subscription;

    @ManyToOne
    private Personnel personnel;

}
