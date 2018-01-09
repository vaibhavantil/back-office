package com.hedvig.backoffice.domain;

import com.hedvig.common.constant.AssetState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
public class Asset {
    @Id
    private String id;

    @NotNull
    private String photoUrl;

    @NotNull
    private String receiptUrl;

    @NotNull
    private String title;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AssetState state;

    @NotNull
    private Boolean includedInBasePackage;

    private String userId;

    @NotNull
    private LocalDate registrationDate;
}
