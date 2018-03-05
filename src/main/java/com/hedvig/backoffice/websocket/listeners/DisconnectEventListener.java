package com.hedvig.backoffice.websocket.listeners;

import com.hedvig.backoffice.services.chat.ChatService;
import com.hedvig.backoffice.services.updates.UpdatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class DisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

    private final ChatService chatService;

    @Autowired
    public DisconnectEventListener(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        chatService.close(headers.getSessionId());
    }

}
