package com.hedvig.backoffice.services.chat;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ChatServiceJob extends QuartzJobBean {

    private ChatUpdatesService service;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            service.update();
        } catch (ChatUpdateException e) {
            throw new JobExecutionException(e);
        }
    }

    public void setService(ChatUpdatesService service) {
        this.service = service;
    }
}
