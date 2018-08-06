package com.hedvig.backoffice.websocket.listeners;

import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.updates.UpdatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Component
public class ConnectEventListener implements ApplicationListener<SessionConnectEvent> {

  private static Logger logger = LoggerFactory.getLogger(ConnectEventListener.class);

  private final UpdatesService updatesService;

  @Autowired
  public ConnectEventListener(UpdatesService updatesService) {
    this.updatesService = updatesService;
  }

  @Override
  public void onApplicationEvent(SessionConnectEvent event) {
    StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
    if (headers.getUser() != null) {
      try {
        updatesService.subscribe(headers.getUser().getName(), headers.getSessionId());
      } catch (AuthorizationException e) {
        logger.error("updates initialization error", e);
      }
    }
  }
}
