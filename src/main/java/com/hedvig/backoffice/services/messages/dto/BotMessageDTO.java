package com.hedvig.backoffice.services.messages.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

@NoArgsConstructor
@Data
public class BotMessageDTO {
  public Long globalId;
  public String id;
  public BotMessageHeaderDTO header;
  public JsonNode body;
  public Instant timestamp;
  public String author;

  public boolean isBotMessage() {
    return Objects.equals(header.getFromId(), 1L);
  }

  public boolean isEmptyBody () {
    return isEmptyField("text") && isEmptyField("URL");
  }

  private boolean isEmptyField(String field) {
    String val = body.path(field).asText();
    return StringUtils.isBlank(val) || "null".equals(val);
}

  static ObjectMapper jsonMapper = new ObjectMapper();
  static {
    jsonMapper.registerModule(new JavaTimeModule());
    jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public String toJson() {
    try {
      return jsonMapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static BotMessageDTO fromJson(String json) {
    try {
      return jsonMapper.readValue(json, BotMessageDTO.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
