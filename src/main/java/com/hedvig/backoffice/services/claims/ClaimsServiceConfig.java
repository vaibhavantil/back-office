package com.hedvig.backoffice.services.claims;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "claims")
@Data
public class ClaimsServiceConfig {

    @Data
    public static class Urls {

        private String claims;
        private String claimById;
        private String claimTypes;
        private String payment;
        private String note;
        private String data;
        private String state;
        private String reserve;
        private String type;

    }

    private String baseUrl;
    private Urls urls;
    private boolean stub;

}
