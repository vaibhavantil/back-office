package com.hedvig.backoffice.websocket.listeners;

import com.hedvig.backoffice.services.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
public class UnsubscribeEventListener implements ApplicationListener<SessionUnsubscribeEvent> {

  private final ChatService chatService;

  @Autowired
  public UnsubscribeEventListener(ChatService chatService) {
    this.chatService = chatService;
  }

  @Override
  public void onApplicationEvent(SessionUnsubscribeEvent event) {
    StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
    String subId = (String) headers.getHeader("simpSubscriptionId");
    chatService.unsubscribe(subId, headers.getSessionId());
  }
}
