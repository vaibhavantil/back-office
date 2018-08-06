package com.hedvig.backoffice.websocket;

import com.hedvig.backoffice.services.chat.ChatService;
import com.hedvig.backoffice.services.messages.dto.BackOfficeResponseDTO;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@MessageMapping("/messages")
public class ChatController {

  private final ChatService chatService;
  private final PersonnelService personnelService;

  @Autowired
  public ChatController(ChatService chatService, PersonnelService personnelService) {
    this.chatService = chatService;
    this.personnelService = personnelService;
  }

  @SubscribeMapping("/send/{memberId}")
  public void send(
      @DestinationVariable String memberId,
      @RequestBody BackOfficeResponseDTO message,
      @AuthenticationPrincipal String principalId) {
    chatService.append(
        memberId, message.getMsg(), principalId, personnelService.getIdToken(principalId));
  }

  @SubscribeMapping("/history/{memberId}")
  public void messages(
      @DestinationVariable String memberId, @AuthenticationPrincipal String principalId) {
    chatService.messages(memberId, principalId, personnelService.getIdToken(principalId));
  }

  @SubscribeMapping("/history/{memberId}/{count}")
  public void messages(
      @DestinationVariable String memberId,
      @DestinationVariable int count,
      @AuthenticationPrincipal String principalId) {
    chatService.messages(memberId, count, principalId, personnelService.getIdToken(principalId));
  }
}
