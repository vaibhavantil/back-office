package com.hedvig.backoffice.websocket;

import com.hedvig.backoffice.services.chat.ChatService;
import com.hedvig.backoffice.services.chat.data.PayloadChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@MessageMapping("/messages")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @SubscribeMapping("/send/{hid}")
    public void send(@DestinationVariable String hid, @RequestBody String body) {
        chatService.append(hid, new PayloadChatMessage(body));
    }

    @SubscribeMapping("/history/{hid}")
    public void messages(@DestinationVariable String hid) {
        chatService.messages(hid);
    }

    @SubscribeMapping("/history/{hid}/{count}")
    public void messages(@DestinationVariable String hid, @DestinationVariable int count) {
        chatService.messages(hid, count);
    }
}
