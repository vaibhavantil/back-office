package com.hedvig.backoffice.graphql.types;

import lombok.Value;

@Value
public class ClaimNote {
  String text;
  String url;

  public static ClaimNote fromDTO(com.hedvig.backoffice.services.claims.dto.ClaimNote dto) {
    return new ClaimNote(dto.getText(), dto.getFileURL());
  }
}
