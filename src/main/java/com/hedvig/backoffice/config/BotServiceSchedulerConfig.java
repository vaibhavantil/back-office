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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
    public JobDetailFactoryBean chatUpdatesJob() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(ChatServiceJob.class);
        jobDetailFactory.setDurability(true);

        JobDataMap data = new JobDataMap();
        data.put(CHAT_SERVICE_VAR, chatUpdatesService);

        jobDetailFactory.setJobDataMap(data);

        return jobDetailFactory;
    }

    @Bean
    public SimpleTriggerFactoryBean chatUpdatesTrigger(JobDetail chatUpdatesJob) {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(chatUpdatesJob);
        trigger.setRepeatInterval(interval);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        return trigger;
    }

    @Bean
    public Executor chatUpdatesExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    public SchedulerFactoryBean botServiceScheduler(Trigger chatUpdatesTrigger, Executor chatUpdatesExecutor) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setJobFactory(new SpringBeanJobFactory());
        schedulerFactory.setTriggers(chatUpdatesTrigger);
        schedulerFactory.setTaskExecutor(chatUpdatesExecutor);
        return schedulerFactory;
    }

}
