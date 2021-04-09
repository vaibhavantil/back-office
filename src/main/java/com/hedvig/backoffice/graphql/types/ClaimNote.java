package com.hedvig.backoffice.graphql.types;

import java.time.LocalDateTime;

import lombok.Value;

@Value
public class ClaimNote {
  String text;
  LocalDateTime date;
  String handlerReference;

  public static ClaimNote fromDTO(com.hedvig.backoffice.services.claims.dto.ClaimNote dto) {
    return new ClaimNote(dto.getText(), dto.getDate(), dto.getHandlerReference());
  }
}
