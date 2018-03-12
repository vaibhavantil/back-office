package com.hedvig.backoffice.config.feign;

import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Configuration
public class FeignConfig {

    private static Logger log = LoggerFactory.getLogger(FeignConfig.class);

    @Bean
    public ErrorDecoder decoder() {
        return (methodKey, response) -> {
            HttpStatus status = HttpStatus.valueOf(response.status());

            if (status == HttpStatus.NOT_FOUND) {
                return new ExternalServiceNotFoundException("entity not found");
            }

            if (status.is4xxClientError()) {
                return new ExternalServiceBadRequestException("bad request");
            }

            StringBuilder buff = new StringBuilder();
            buff.append("request to ").append(response.request().url()).append(" failed ");

            try {
                if (response.body() != null) {
                    buff.append("cause: ").append(IOUtils.toString(response.body().asReader()));
                }
            } catch (IOException e) {
                log.error("request body can't parsed", e);
            }

            log.error(buff.toString());
            return new RuntimeException();
        };
    }

}
