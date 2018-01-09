package com.hedvig.backoffice.chat;

import com.hedvig.backoffice.chat.dto.MessageDTO;
import com.hedvig.backoffice.services.chat.ChatService;
import com.hedvig.backoffice.services.users.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/messages")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @SubscribeMapping("/send/{id}")
    public void send(@DestinationVariable String id, MessageDTO message) throws UserNotFoundException {
        chatService.retranslate(id, message);
    }

}
