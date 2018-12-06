package com.hedvig.backoffice.services.chat.data;

import java.util.List;

import com.hedvig.backoffice.web.dto.BotMessageWebDTO;
import lombok.Value;

@Value
public class ChatMessages implements MessagePayload {

  private List<BotMessageWebDTO> messages;
}
