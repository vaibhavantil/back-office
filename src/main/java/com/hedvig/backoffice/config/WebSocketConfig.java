package com.hedvig.backoffice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

  private int maxFileSize;

  public WebSocketConfig(@Value("${websocket.max-sile-size}") int maxFileSize) {
    this.maxFileSize = maxFileSize;
  }

  @Override
  protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
    messages.simpDestMatchers("/topic/**", "/user/**").authenticated().anyMessage().authenticated();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic", "/user");
    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/chat").setAllowedOrigins("*").withSockJS();
  }

  @Override
  public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
    registration.setSendBufferSizeLimit(maxFileSize);
    registration.setMessageSizeLimit(maxFileSize);
  }

  @Override
  protected boolean sameOriginDisabled() {
    return true;
  }
}
