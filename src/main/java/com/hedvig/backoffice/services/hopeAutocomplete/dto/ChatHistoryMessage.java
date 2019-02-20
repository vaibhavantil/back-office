package com.hedvig.backoffice.services.hopeAutocomplete.dto;

import lombok.Data;

@Data
public class ChatHistoryMessage {
  public final String authorType;
  public final String text;
  public final String timestamp;
}
