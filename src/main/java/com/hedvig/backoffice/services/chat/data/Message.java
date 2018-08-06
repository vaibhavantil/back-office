package com.hedvig.backoffice.services.chat.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Value
public class Message {

  public static Logger logger = LoggerFactory.getLogger(Message.class);

  private MessageType type;

  private Instant timestamp;

  private MessagePayload payload;

  public String toJson() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    try {
      return mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return "";
    }
  }

  public static Message error(int code, String msg) {
    return new Message(MessageType.ERROR, new Date().toInstant(), new ErrorMessage(code, msg));
  }

  public static Message chat(List<BotMessage> messages) {
    return new Message(
        MessageType.MESSAGE,
        new Date().toInstant(),
        new ChatMessages(
            messages
                .stream()
                .filter(Objects::nonNull)
                .map(BotMessage::getMessage)
                .collect(Collectors.toList())));
  }
}
