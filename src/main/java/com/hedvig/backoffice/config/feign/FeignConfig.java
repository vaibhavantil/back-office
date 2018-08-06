package com.hedvig.backoffice.config.feign;

import feign.Request;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class FeignConfig {

  private static Logger log = LoggerFactory.getLogger(FeignConfig.class);

  @Bean
  public ErrorDecoder decoder() {
    return (methodKey, response) -> {
      HttpStatus status = HttpStatus.valueOf(response.status());

      StringBuilder buff = new StringBuilder();
      buff.append("request to ").append(response.request().url()).append(" failed ");

      try {
        if (response.body() != null) {
          buff.append("cause: ").append(IOUtils.toString(response.body().asReader()));
        }
      } catch (IOException e) {
        log.error("request body can't parsed", e);
      }

      if (status == HttpStatus.NOT_FOUND) {
        return new ExternalServiceNotFoundException("entity not found", buff.toString());
      }

      if (status == HttpStatus.UNAUTHORIZED) {
        return new ExternalServiceUnauthorizedException("unauthorized", buff.toString());
      }

      if (status.is4xxClientError()) {
        return new ExternalServiceBadRequestException("bad request", buff.toString());
      }

      log.error(buff.toString());
      return new ExternalServiceException(buff.toString());
    };
  }

  @Bean
  public Request.Options options() {
    return new Request.Options(10 * 1000, 300 * 1000);
  }

  @Bean
  public feign.Logger.Level feignLogger() {
    return feign.Logger.Level.NONE;
  }
}
