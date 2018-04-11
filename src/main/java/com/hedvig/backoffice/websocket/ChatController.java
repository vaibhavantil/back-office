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

    @SubscribeMapping("/send/{hid}")
    public void send(@DestinationVariable String hid, @RequestBody BackOfficeResponseDTO message, @AuthenticationPrincipal String principalId) {
        chatService.append(hid, message.getMsg(), personnelService.getIdToken(principalId));
    }

    @SubscribeMapping("/history/{hid}")
    public void messages(@DestinationVariable String hid, @AuthenticationPrincipal String principalId) {
        chatService.messages(hid, personnelService.getIdToken(principalId));
    }

    @SubscribeMapping("/history/{hid}/{count}")
    public void messages(@DestinationVariable String hid, @DestinationVariable int count, @AuthenticationPrincipal String principalId) {
        chatService.messages(hid, count, personnelService.getIdToken(principalId));
    }
}
