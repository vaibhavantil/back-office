package com.hedvig.backoffice.services.chat;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ChatServiceJob extends QuartzJobBean {

    private static Logger logger = LoggerFactory.getLogger(ChatServiceJob.class);

    private ChatUpdatesService service;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        service.update();
    }

    public void setService(ChatUpdatesService service) {
        this.service = service;
    }
}
