package com.hedvig.backoffice.domain;

import com.hedvig.common.constant.AssetState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDate;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
public class Asset {
    @Id
    private String id;

    private String photoUrl;

    private String receiptUrl;

    private String title;

    @Enumerated(EnumType.STRING)
    private AssetState state;

    private Boolean includedInBasePackage;

    private String userId;

    private LocalDate registrationDate;
}
