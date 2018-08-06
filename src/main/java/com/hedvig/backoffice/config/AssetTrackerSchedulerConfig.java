package com.hedvig.backoffice.config;

import com.hedvig.backoffice.services.assettracker.AssetTrackerJob;
import com.hedvig.backoffice.services.assettracker.AssetTrackerService;
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
public class AssetTrackerSchedulerConfig {

  public static final String ASSET_TRACKER_SERVICE_VAR = "service";

  @Value("${intervals.tracker}")
  private int interval;

  private AssetTrackerService service;

  @Autowired
  public AssetTrackerSchedulerConfig(AssetTrackerService service) {
    this.service = service;
  }

  @Bean
  public JobDetailFactoryBean assetTrackerJob() {
    JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
    jobDetailFactory.setJobClass(AssetTrackerJob.class);
    jobDetailFactory.setDurability(true);

    JobDataMap data = new JobDataMap();
    data.put(ASSET_TRACKER_SERVICE_VAR, service);
    jobDetailFactory.setJobDataMap(data);

    return jobDetailFactory;
  }

  @Bean
  public SimpleTriggerFactoryBean assetTrackerTrigger(JobDetail assetTrackerJob) {
    SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
    trigger.setJobDetail(assetTrackerJob);
    trigger.setRepeatInterval(interval);
    trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    return trigger;
  }

  @Bean
  public SchedulerFactoryBean assetTrackerScheduler(
      Trigger assetTrackerTrigger, JobDetail assetTrackerJob) {
    SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
    schedulerFactory.setJobFactory(new SpringBeanJobFactory());
    schedulerFactory.setJobDetails(assetTrackerJob);
    schedulerFactory.setTriggers(assetTrackerTrigger);
    return schedulerFactory;
  }
}
