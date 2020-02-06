package com.hedvig.backoffice.websocket;

import com.hedvig.backoffice.security.GatekeeperUser;
import com.hedvig.backoffice.services.chat.ChatService;
import com.hedvig.backoffice.services.messages.dto.BackOfficeResponseDTO;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@MessageMapping("/messages")
public class MessagesController {

  private final ChatService chatService;
  private final PersonnelService personnelService;

  @Autowired
  public MessagesController(ChatService chatService, PersonnelService personnelService) {
    this.chatService = chatService;
    this.personnelService = personnelService;
  }

  @MessageMapping("/send/{memberId}")
  public void send(
      @DestinationVariable String memberId,
      @RequestBody BackOfficeResponseDTO message,
      @AuthenticationPrincipal GatekeeperUser principal) {
    chatService.append(
        memberId, message.getMsg(), message.forceSendMessage, principal.getUsername(), personnelService.getIdToken(principal.getUsername()));
  }

  @MessageMapping("/history/{memberId}")
  public void messages(
      @DestinationVariable String memberId, @AuthenticationPrincipal GatekeeperUser principal) {
    chatService.messages(memberId, principal.getUsername(), personnelService.getIdToken(principal.getUsername()));
  }

  @MessageMapping("/history/{memberId}/{count}")
  public void messages(
      @DestinationVariable String memberId,
      @DestinationVariable int count,
      @AuthenticationPrincipal GatekeeperUser principal) {
    chatService.messages(memberId, count, principal.getUsername(), personnelService.getIdToken(principal.getUsername()));
  }
}
