package com.hedvig.backoffice.web.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.services.messages.dto.BotMessageDTO;
import com.hedvig.backoffice.services.messages.dto.BotMessageHeaderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

import static com.hedvig.backoffice.util.TzHelper.SWEDEN_TZ;
import static com.hedvig.backoffice.util.TzHelper.toLocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotMessageWebDTO {
  Long globalId;
  String id;
  BotMessageHeaderDTO header;
  JsonNode body;
  Instant timestamp;
  LocalDateTime localTimestamp;

  public BotMessageWebDTO(BotMessageDTO msg) {
    this.globalId = msg.getGlobalId();
    this.id = msg.getId();
    this.header = msg.getHeader();
    this.body = msg.getBody();
    this.timestamp = msg.getTimestamp();
    this.localTimestamp = toLocalDateTime(msg.getTimestamp(), SWEDEN_TZ);
  }
}
