package com.hedvig.backoffice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
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
    private String memberId;

    public Subscription(String memberId) {
        this.memberId = memberId;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "subscription", cascade = CascadeType.REMOVE)
    private Set<ChatContext> chats = new HashSet<>();

}
