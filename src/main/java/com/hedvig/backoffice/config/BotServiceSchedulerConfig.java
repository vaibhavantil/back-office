package com.hedvig.backoffice.config;

import com.hedvig.backoffice.services.chat.ChatServiceJob;
import com.hedvig.backoffice.services.chat.ChatUpdatesService;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
public class BotServiceSchedulerConfig {

    public static final String CHAT_SERVICE_VAR = "service";

    @Value("${intervals.chat}")
    private int interval;

    private final ChatUpdatesService chatUpdatesService;

    @Autowired
    public BotServiceSchedulerConfig(ChatUpdatesService chatUpdatesService) {
        this.chatUpdatesService = chatUpdatesService;
    }

    @Bean
    public JobDetailFactoryBean chatTrackerJob() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(ChatServiceJob.class);
        jobDetailFactory.setDurability(true);

        JobDataMap data = new JobDataMap();
        data.put(CHAT_SERVICE_VAR, chatUpdatesService);

        jobDetailFactory.setJobDataMap(data);

        return jobDetailFactory;
    }

    @Bean
    public SimpleTriggerFactoryBean chatTrackerTrigger(JobDetail chatTrackerJob) {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(chatTrackerJob);
        trigger.setRepeatInterval(interval);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        return trigger;
    }

    @Bean
    public SchedulerFactoryBean botServiceScheduler(Trigger chatTrackerTrigger, JobDetail chatTrackerJob) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setJobFactory(new SpringBeanJobFactory());
        schedulerFactory.setJobDetails(chatTrackerJob);
        schedulerFactory.setTriggers(chatTrackerTrigger);
        return schedulerFactory;
    }

}
