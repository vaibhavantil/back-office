package com.hedvig.backoffice.config;

import com.hedvig.backoffice.services.assettracker.AssetTracker;
import com.hedvig.backoffice.services.assettracker.AssetTrackerStub;
import com.hedvig.backoffice.services.assettracker.AssetTrackerImpl;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.ClaimsServiceImpl;
import com.hedvig.backoffice.services.claims.ClaimsServiceStub;
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

    private final ApplicationContext context;

    @Autowired
    public ExternalServicesConfig(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public AssetTracker assetTracker(@Value("${tracker.stub:false}") boolean stub) {
        val factory = context.getAutowireCapableBeanFactory();

        return stub
                ? factory.createBean(AssetTrackerStub.class)
                : factory.createBean(AssetTrackerImpl.class);
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

    @Bean
    public ClaimsService claimsService(@Value("${claims.stub:false}") boolean stub) {
        val factory = context.getAutowireCapableBeanFactory();

        return stub
                ? factory.createBean(ClaimsServiceStub.class)
                : factory.createBean(ClaimsServiceImpl.class);
    }

}
