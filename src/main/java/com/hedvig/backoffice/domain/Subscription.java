package com.hedvig.backoffice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String hid;

    public Subscription(String hid) {
        this.hid = hid;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "subscription", cascade = CascadeType.REMOVE)
    private Set<ChatContext> chats = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapKey(name = "messageId")
    private Map<Long, MessageInfo> messages = new HashMap<>();

}
