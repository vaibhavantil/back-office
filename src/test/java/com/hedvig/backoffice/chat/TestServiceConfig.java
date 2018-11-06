package com.hedvig.backoffice.chat;

import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceClient;
import com.hedvig.backoffice.services.messages.BotServiceImpl;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestServiceConfig {
  private final ApplicationContext context;

  @Autowired
  public TestServiceConfig(ApplicationContext context) {
    this.context = context;
  }

  @Bean
  public BotServiceClient botServiceClient() {
    val factory = context.getAutowireCapableBeanFactory();

    return factory.createBean(BotServiceClientStub.class);
  }

  @Bean
  public BotService botService() {
    val factory = context.getAutowireCapableBeanFactory();

    return factory.createBean(BotServiceImpl.class);
  }

}
