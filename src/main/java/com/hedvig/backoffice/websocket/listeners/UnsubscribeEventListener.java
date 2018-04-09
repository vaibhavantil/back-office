package com.hedvig.backoffice.websocket.listeners;

import com.hedvig.backoffice.services.chat.ChatService;
import com.hedvig.backoffice.services.updates.UpdatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
public class UnsubscribeEventListener implements ApplicationListener<SessionUnsubscribeEvent> {

    private final ChatService chatService;
    private final UpdatesService updatesService;

    @Autowired
    public UnsubscribeEventListener(ChatService chatService, UpdatesService updatesService) {
        this.chatService = chatService;
        this.updatesService = updatesService;
    }

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        String subId = (String) headers.getHeader("simpSubscriptionId");
        chatService.unsubscribe(subId, headers.getSessionId());
        updatesService.unsubscribe(headers.getUser().getName(), headers.getSessionId(), subId);
    }

}
