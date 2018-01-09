package com.hedvig.backoffice.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hedvig.backoffice.domain.Asset;
import com.hedvig.common.constant.AssetState;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
public class AssetDTO {
    @NotNull
    String id;

    @NotNull
    String photoUrl;

    @NotNull
    String receiptUrl;

    @NotNull
    String title;

    @NotNull
    AssetState state;

    @NotNull
    Boolean includedInBasePackage;

    @NotNull
    String userId;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate registrationDate;

    public static AssetDTO fromDomain(Asset asset) {
        return new AssetDTO(
                asset.getId(),
                asset.getPhotoUrl(),
                asset.getReceiptUrl(),
                asset.getTitle(),
                asset.getState(),
                asset.getIncludedInBasePackage(),
                asset.getUserId(),
                asset.getRegistrationDate());
    }

    public static Asset toDomain(AssetDTO dto) {
        return new Asset(
                dto.id,
                dto.photoUrl,
                dto.receiptUrl,
                dto.title,
                dto.state,
                dto.includedInBasePackage,
                dto.userId,
                dto.registrationDate
        );
    }
}
