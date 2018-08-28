package com.hedvig.backoffice.websocket.listeners;

import com.hedvig.backoffice.services.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class SubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

  private final ChatService chatService;

  @Autowired
  public SubscribeEventListener(ChatService chatService) {
    this.chatService = chatService;
  }

  @Override
  public void onApplicationEvent(SessionSubscribeEvent event) {
    StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
    String destination = headers.getDestination();
    String subId = (String) headers.getHeader("simpSubscriptionId");
    int index = destination.indexOf(chatService.getTopicPrefix());
    if (index >= 0) {
      String memberId = destination.substring(index + chatService.getTopicPrefix().length());
      chatService.subscribe(memberId, subId, headers.getSessionId(), headers.getUser().getName());
    }
  }
}
