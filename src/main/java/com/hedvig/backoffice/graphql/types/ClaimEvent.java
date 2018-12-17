package com.hedvig.backoffice.graphql.types;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.Value;

@Value
public class ClaimEvent {
  String text;
  ZonedDateTime date;

  public static ClaimEvent fromDTO(com.hedvig.backoffice.services.claims.dto.ClaimEvent dto) {
    return new ClaimEvent(dto.getText(), dto.getDate().atZone(ZoneId.of("Europe/Stockholm")));
  }
}
