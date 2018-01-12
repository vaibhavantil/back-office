package com.hedvig.backoffice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.services.users.UserService;
import com.hedvig.backoffice.services.users.UserServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class BackOfficeStartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(BackOfficeStartupApplicationListener.class);

    private final UserService userService;

    @Autowired
    public BackOfficeStartupApplicationListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            logger.info("TEST USER SERVICE:");
            ObjectMapper mapper = new ObjectMapper();
            userService.list()
                    .stream()
                    .limit(10)
                    .forEach(u -> {
                        try {
                            logger.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(u));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (UserServiceException e) {
            e.printStackTrace();
        }
    }

}
