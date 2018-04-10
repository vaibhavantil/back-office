package com.hedvig.backoffice.domain;

import com.hedvig.backoffice.services.updates.UpdateType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Updates {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private UpdateType type;

    @ManyToOne
    private UpdateContext context;

    @NotNull
    @OneToOne
    private Personnel personnel;

    @NotNull
    private Long count;

    public Updates(UpdateType type, UpdateContext context, Long count) {
        this.type = type;
        this.context = context;
        this.count = count;
        this.personnel = context.getPersonnel();
    }

}
