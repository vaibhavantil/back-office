package com.hedvig.backoffice.websocket;

import com.hedvig.backoffice.services.messages.data.Message;
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

    @SubscribeMapping("/send/{hid}")
    public void send(@DestinationVariable String hid, Message message) throws UserNotFoundException {
        chatService.append(hid, message);
    }

}
