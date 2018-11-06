package com.hedvig.backoffice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.hedvig.backoffice")
@EnableFeignClients
@ComponentScan(basePackages = "com.hedvig.backoffice")
public class IntegrationTestApplication {
}
