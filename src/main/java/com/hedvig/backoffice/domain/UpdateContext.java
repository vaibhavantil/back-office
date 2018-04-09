package com.hedvig.backoffice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UpdateContext {

    @Id
    @GeneratedValue
    private Long id;

    private String sessionId;

    private String subId;

    @NotNull
    @OneToOne
    private Personnel personnel;

    private boolean active = false;

    public UpdateContext(Personnel personnel) {
        this.personnel = personnel;
    }

}
