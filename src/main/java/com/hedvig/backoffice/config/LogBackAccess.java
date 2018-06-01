package com.hedvig.backoffice.config;

import ch.qos.logback.access.tomcat.LogbackValve;
import org.apache.catalina.Valve;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;


@Configuration
public class LogBackAccess {

    @Bean(name = "TeeFilter")
    public Filter teeFilter() {
        return new ch.qos.logback.access.servlet.TeeFilter();
    }

    @Bean
    public Valve logbackValve() {

        LogbackValve logbackValve = new LogbackValve();

        // point to logback-access.xml
        logbackValve.setFilename("logback-access.xml");


        return logbackValve;
    }

}
