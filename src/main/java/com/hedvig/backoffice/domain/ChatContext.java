package com.hedvig.backoffice.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
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

}
