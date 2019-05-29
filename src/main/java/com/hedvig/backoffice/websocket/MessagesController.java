package com.hedvig.backoffice.websocket;

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
      @AuthenticationPrincipal String principalId) {
    chatService.append(
        memberId, message.getMsg(), false, principalId, personnelService.getIdToken(principalId));
  }

  @MessageMapping("/history/{memberId}")
  public void messages(
      @DestinationVariable String memberId, @AuthenticationPrincipal String principalId) {
    chatService.messages(memberId, principalId, personnelService.getIdToken(principalId));
  }

  @MessageMapping("/history/{memberId}/{count}")
  public void messages(
      @DestinationVariable String memberId,
      @DestinationVariable int count,
      @AuthenticationPrincipal String principalId) {
    chatService.messages(memberId, count, principalId, personnelService.getIdToken(principalId));
  }
}
