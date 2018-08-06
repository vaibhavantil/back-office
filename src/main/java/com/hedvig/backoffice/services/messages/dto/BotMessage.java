package com.hedvig.backoffice.services.messages.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hedvig.backoffice.services.messages.BotMessageException;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class BotMessage {

  private JsonNode message;

  private Instant timestamp;

  private Long globalId;

  private Long messageId;

  private Long fromId;

  private JsonNode body;

  private JsonNode header;

  private String type;

  private String id;

  private String memberId;

  public BotMessage(String message) throws BotMessageException {
    this(message, false);
  }

  public BotMessage(String message, boolean newMessage) throws BotMessageException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    try {
      this.message = mapper.readValue(message, JsonNode.class);
    } catch (IOException e) {
      throw new BotMessageException(e);
    }

    parseFields(newMessage);
  }

  public BotMessage(JsonNode message) throws BotMessageException {
    this(message, false);
  }

  public BotMessage(JsonNode message, boolean newMessage) throws BotMessageException {
    this.message = message;
    parseFields(newMessage);
  }

  public boolean isBotMessage() {
    return Objects.equals(fromId, 1L);
  }

  public void setGlobalId(Long globalId) {
    this.globalId = globalId;
    ObjectNode root = (ObjectNode) this.message;
    root.put("globalId", globalId);
  }

  public void setMessageId(Long messageId) {
    this.messageId = messageId;
    ObjectNode header = (ObjectNode) this.header;
    header.put("messageId", messageId);
  }

  public void setMemberId(String memberId) {
    this.memberId = memberId;
  }

  private void parseFields(boolean newMessage) throws BotMessageException {
    header =
        Optional.ofNullable(message.get("header"))
            .orElseThrow(() -> new BotMessageException("message must contains header"));

    body =
        Optional.ofNullable(message.get("body"))
            .orElseThrow(() -> new BotMessageException("message must contains body"));

    type =
        Optional.ofNullable(body.get("type"))
            .map(JsonNode::asText)
            .orElseThrow(() -> new BotMessageException("message must contains type"));

    if (!newMessage) {
      globalId =
          Optional.ofNullable(message.get("globalId"))
              .orElseThrow(() -> new BotMessageException("message must contains globalId"))
              .asLong();

      messageId =
          Optional.ofNullable(header.get("messageId"))
              .orElseThrow(() -> new BotMessageException("message header must contains globalId"))
              .asLong();

      fromId =
          Optional.ofNullable(header.get("fromId"))
              .orElseThrow(() -> new BotMessageException("message header must contains fromId"))
              .asLong();

      id = Optional.ofNullable(message.get("id")).map(JsonNode::asText).orElse("");
    }

    JsonNode value =
        Optional.ofNullable(message.get("timestamp"))
            .orElseThrow(() -> new BotMessageException("message must contains timestamp"));

    String timeStr = value.asText();
    if (StringUtils.isBlank(timeStr)) {
      throw new BotMessageException("message must contains timestamp");
    }

    try {
      timestamp = Instant.parse(timeStr);
    } catch (DateTimeParseException e) {
      throw new BotMessageException(e);
    }
  }
}
