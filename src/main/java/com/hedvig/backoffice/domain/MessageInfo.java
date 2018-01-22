package com.hedvig.backoffice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class MessageInfo {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Long messageId;

    @NotNull
    private Instant timestamp;

    public MessageInfo(long messageId, Instant timestamp) {
        this.messageId = messageId;
        this.timestamp = timestamp;
    }

}
