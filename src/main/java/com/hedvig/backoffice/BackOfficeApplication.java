package com.hedvig.backoffice;

import com.hedvig.backoffice.security.SecureFilter;
import java.util.List;
import org.apache.catalina.Valve;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.hedvig")
@EnableFeignClients
@EnableCircuitBreaker
@EnableScheduling
public class BackOfficeApplication {

  public static void main(String[] args) {
    SpringApplication.run(BackOfficeApplication.class, args);
  }

  @Value("${server.port:8443}")
  private int defaultPost;

  @Value("${server.httpPort:8080}")
  private int httpPost;

  @Value("${server.httpsRedirect:true}")
  private boolean httpsRedirect;

  @Bean
  public ServletWebServerFactory servletContainer(List<Valve> valves) {
    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
    tomcat.addAdditionalTomcatConnectors(httpConnector());

    for (Valve v : valves) {
      tomcat.addContextValves(v);
    }

    return tomcat;
  }

  private Connector httpConnector() {
    Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
    connector.setScheme("http");
    connector.setPort(httpPost);
    connector.setSecure(false);
    return connector;
  }

  @Bean
  public FilterRegistrationBean secureFilterBean() {
    final FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
    filterRegBean.setFilter(new SecureFilter(defaultPost, httpPost, httpsRedirect));
    filterRegBean.addUrlPatterns("/*");
    filterRegBean.setEnabled(Boolean.TRUE);
    filterRegBean.setAsyncSupported(Boolean.TRUE);
    return filterRegBean;
  }

  @Configuration
  @Profile("development")
  @ComponentScan(lazyInit = true)
  static class DevConfig {
  }
}
