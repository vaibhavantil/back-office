package com.hedvig.backoffice.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Value("${amqp.hostname}")
    private String hostname;

    @Bean
    public ConnectionFactory amqpConnectionFactory() {
        return new CachingConnectionFactory(hostname);
    }

}
