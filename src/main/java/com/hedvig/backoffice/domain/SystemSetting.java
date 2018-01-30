package com.hedvig.backoffice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SystemSetting {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private SystemSettingType type;

    @NotNull
    private String value;

    public SystemSetting(SystemSettingType type, String value) {
        this.type = type;
        this.value = value;
    }
}
