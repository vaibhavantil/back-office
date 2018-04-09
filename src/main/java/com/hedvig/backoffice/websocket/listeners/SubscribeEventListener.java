package com.hedvig.backoffice.websocket.listeners;

import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.chat.ChatService;
import com.hedvig.backoffice.services.updates.UpdatesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Slf4j
@Component
public class SubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    private final ChatService chatService;
    private final UpdatesService updatesService;

    @Autowired
    public SubscribeEventListener(ChatService chatService, UpdatesService updatesService) {
        this.chatService = chatService;
        this.updatesService = updatesService;
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headers.getDestination();
        String subId = (String) headers.getHeader("simpSubscriptionId");
        if (destination.startsWith(chatService.getTopicPrefix())) {
            String hid = destination.substring(chatService.getTopicPrefix().length());
            chatService.subscribe(hid, subId, headers.getSessionId(), headers.getUser().getName());
        } else if (destination.startsWith("/user")) {
            try {
                updatesService.subscribe(headers.getUser().getName(), headers.getSessionId(), subId);
            } catch (AuthorizationException e) {
                log.error("unable to subscribe user", e);
            }
        }

    }

}
