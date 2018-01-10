package com.hedvig.backoffice.config;

import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceImpl;
import com.hedvig.backoffice.services.messages.BotServiceStub;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotServiceConfig {

    @Autowired
    private ApplicationContext context;

    @Bean
    public BotService botService(@Value("${botservice.stub:false}") boolean stub) {
        val factory = context.getAutowireCapableBeanFactory();

        return stub
                ? factory.createBean(BotServiceStub.class)
                : factory.createBean(BotServiceImpl.class);
    }


}
