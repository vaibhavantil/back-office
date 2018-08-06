package com.hedvig.backoffice.services.expo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpoNotificationServiceStub implements ExpoNotificationService {

  private static Logger logger = LoggerFactory.getLogger(ExpoNotificationServiceStub.class);

  @Override
  public void sendNotification(String memberId, String token) {
    logger.info(String.format("Attempting to send push notification to memberId %s", memberId));
  }
}
