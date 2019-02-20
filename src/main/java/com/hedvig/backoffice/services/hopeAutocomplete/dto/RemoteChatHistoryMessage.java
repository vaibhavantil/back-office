package com.hedvig.backoffice.services.hopeAutocomplete.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RemoteChatHistoryMessage {
  public final String authorType;
  public final String text;
  public final long timestamp;

  public static List<RemoteChatHistoryMessage> from(final List<ChatHistoryMessage> chatHistory) {
    return chatHistory.stream()
      .map(RemoteChatHistoryMessage::from)
      .collect(toList());
  }

  public static RemoteChatHistoryMessage from(final ChatHistoryMessage message) {
    return new RemoteChatHistoryMessage(message.authorType, message.text, Instant.parse(message.timestamp).getEpochSecond());
  }
}
