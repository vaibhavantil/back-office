package com.hedvig.backoffice.config;

import com.hedvig.backoffice.services.assettracker.AssetTracker;
import com.hedvig.backoffice.services.assettracker.LocalAssetTracker;
import com.hedvig.backoffice.services.assettracker.RemoteAssetTracker;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssetTrackerConfig {

    @Autowired
    private ApplicationContext context;

    @Bean
    public AssetTracker assetTracker(@Value("${tracker.stub:false}") boolean stub) {
        val factory = context.getAutowireCapableBeanFactory();

        AssetTracker tracker = stub
                ? factory.createBean(LocalAssetTracker.class)
                : factory.createBean(RemoteAssetTracker.class);

        return tracker;
    }

}
