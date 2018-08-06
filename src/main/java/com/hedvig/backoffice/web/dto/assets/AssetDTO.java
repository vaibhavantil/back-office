package com.hedvig.backoffice.web.dto.assets;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hedvig.backoffice.domain.Asset;
import com.hedvig.common.constant.AssetState;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Value;

@Value
public class AssetDTO {
  @NotNull String id;

  String photoUrl;

  String receiptUrl;

  String title;

  AssetState state;

  Boolean includedInBasePackage;

  String userId;

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
        Optional.ofNullable(dto.photoUrl).orElse(""),
        Optional.ofNullable(dto.receiptUrl).orElse(""),
        Optional.ofNullable(dto.title).orElse(""),
        Optional.ofNullable(dto.state).orElse(AssetState.PENDING),
        Optional.ofNullable(dto.includedInBasePackage).orElse(false),
        Optional.ofNullable(dto.userId).orElse(""),
        Optional.ofNullable(dto.registrationDate)
            .orElse(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
  }
}
