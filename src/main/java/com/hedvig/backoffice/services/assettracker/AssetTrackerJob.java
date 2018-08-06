package com.hedvig.backoffice.services.assettracker;

import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class AssetTrackerJob extends QuartzJobBean {

  private AssetTrackerService service;

  @Override
  protected void executeInternal(JobExecutionContext context) {
    service.loadPendingAssetsFromTracker();
  }

  public void setService(AssetTrackerService service) {
    this.service = service;
  }
}
