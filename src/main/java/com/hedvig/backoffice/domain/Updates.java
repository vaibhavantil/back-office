package com.hedvig.backoffice.domain;

import com.hedvig.backoffice.services.updates.UpdateType;
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
public class Updates {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private UpdateType type;

    @NotNull
    @OneToOne
    private Personnel personnel;

    @NotNull
    private Long count;

    public Updates(UpdateType type, Personnel personnel, Long count) {
        this.type = type;
        this.personnel = personnel;
        this.count = count;
    }

}
