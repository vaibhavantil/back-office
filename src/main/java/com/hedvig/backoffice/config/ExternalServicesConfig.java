package com.hedvig.backoffice.config;

import com.hedvig.backoffice.services.assettracker.AssetTracker;
import com.hedvig.backoffice.services.assettracker.AssetTrackerStub;
import com.hedvig.backoffice.services.assettracker.AssetTrackerImpl;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.ClaimsServiceConfig;
import com.hedvig.backoffice.services.claims.ClaimsServiceImpl;
import com.hedvig.backoffice.services.claims.ClaimsServiceStub;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceImpl;
import com.hedvig.backoffice.services.messages.BotServiceStub;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.members.MemberServiceImpl;
import com.hedvig.backoffice.services.members.MemberServiceStub;
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
    public MemberService memberService(@Value("${memberservice.stub:false}") boolean stub) {
        val factory = context.getAutowireCapableBeanFactory();

        return stub
                ? factory.createBean(MemberServiceStub.class)
                : factory.createBean(MemberServiceImpl.class);
    }

    @Bean
    public ClaimsService claimsService(ClaimsServiceConfig config) {
        val factory = context.getAutowireCapableBeanFactory();

        return config.isStub()
                ? factory.createBean(ClaimsServiceStub.class)
                : factory.createBean(ClaimsServiceImpl.class);
    }

}
