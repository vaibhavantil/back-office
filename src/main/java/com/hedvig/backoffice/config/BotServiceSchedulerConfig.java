package com.hedvig.backoffice.config;

import com.hedvig.backoffice.repository.ChatContextRepository;
import com.hedvig.backoffice.repository.SubscriptionRepository;
import com.hedvig.backoffice.services.chat.ChatService;
import com.hedvig.backoffice.services.chat.ChatServiceJob;
import com.hedvig.backoffice.services.messages.BotService;
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

    public static final String CHAT_SERVICE_VAR = "chatService";
    public static final String SUB_REPOSITORY_VAR = "subscriptionRepository";
    public static final String MESSAGE_SERVICE_VER = "botService";

    @Value("${intervals.chat}")
    private int interval;

    private final ChatService chatService;
    private final ChatContextRepository chatContextRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final BotService botService;

    @Autowired
    public BotServiceSchedulerConfig(ChatService chatService,
                                     ChatContextRepository chatContextRepository,
                                     BotService botService,
                                     SubscriptionRepository subscriptionRepository) {

        this.chatService = chatService;
        this.chatContextRepository = chatContextRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.botService = botService;
    }

    @Bean
    public JobDetailFactoryBean chatTrackerJob() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(ChatServiceJob.class);
        jobDetailFactory.setDurability(true);

        JobDataMap data = new JobDataMap();
        data.put(CHAT_SERVICE_VAR, chatService);
        data.put(SUB_REPOSITORY_VAR, subscriptionRepository);
        data.put(MESSAGE_SERVICE_VER, botService);

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
