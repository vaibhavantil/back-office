package com.hedvig.backoffice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Personnel {
    @Id
    private String id;

    private String email;

    private String name;

    private String picture;

    public Personnel(String id) {
        this.id = id;
    }
}
