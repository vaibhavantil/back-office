package com.hedvig.backoffice.config;

import com.hedvig.backoffice.services.assettracker.AssetTracker;
import com.hedvig.backoffice.services.assettracker.LocalAssetTracker;
import com.hedvig.backoffice.services.assettracker.RemoteAssetTracker;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceImpl;
import com.hedvig.backoffice.services.messages.BotServiceStub;
import com.hedvig.backoffice.services.users.UserService;
import com.hedvig.backoffice.services.users.UserServiceImpl;
import com.hedvig.backoffice.services.users.UserServiceStub;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalServicesConfig {

    @Autowired
    private ApplicationContext context;

    @Bean
    public AssetTracker assetTracker(@Value("${tracker.stub:false}") boolean stub) {
        val factory = context.getAutowireCapableBeanFactory();

        return stub
                ? factory.createBean(LocalAssetTracker.class)
                : factory.createBean(RemoteAssetTracker.class);
    }

    @Bean
    public BotService botService(@Value("${botservice.stub:false}") boolean stub) {
        val factory = context.getAutowireCapableBeanFactory();

        return stub
                ? factory.createBean(BotServiceStub.class)
                : factory.createBean(BotServiceImpl.class);
    }

    @Bean
    public UserService userService(@Value("${userservice.stub:false}") boolean stub) {
        val factory = context.getAutowireCapableBeanFactory();

        return stub
                ? factory.createBean(UserServiceStub.class)
                : factory.createBean(UserServiceImpl.class);
    }

}
