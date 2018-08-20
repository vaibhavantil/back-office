package com.hedvig.backoffice.services.expo;

import com.hedvig.backoffice.services.messages.BotService;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpoNotificationServiceImpl implements ExpoNotificationService {

  private static Logger logger = LoggerFactory.getLogger(ExpoNotificationServiceImpl.class);

  private final BotService botService;
  private final ExpoClient expoClient;

  public ExpoNotificationServiceImpl(BotService botService, ExpoClient expoClient) {
    this.botService = botService;
    this.expoClient = expoClient;
  }

  @Override
  public void sendNotification(String memberId, String token) {
    try {
      val pushToken = botService.getExpoPushToken(memberId, token);
      if (pushToken == null || pushToken.getToken() == null) {
        logger.error("Member does not have push token, id = " + memberId);
        return;
      }

      val dto =
          new ExpoPushDTO(
              pushToken.getToken(), "Hedvig", "Hej, du har ett nytt meddelande från Hedvig!");
      logger.info(
          "Attempting  to send push to user with id: {}, body: {}", memberId, dto.toString());
      val result = expoClient.sendPush(dto);
      logger.info(
          "Got result from expo for push notification to user with id: {}, body: {}",
          memberId,
          result);
    } catch (Exception e) {
      logger.error("Error, could not send push notification through expo", e);
    }
  }
}
