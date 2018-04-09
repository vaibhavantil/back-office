package com.hedvig.backoffice.services.expo;

import com.hedvig.backoffice.services.messages.BotService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.val;

public class ExpoNotificationServiceImpl implements ExpoNotificationService {

    private static Logger logger = LoggerFactory.getLogger(ExpoNotificationServiceImpl.class);

    private final BotService botService;
    private final ExpoClient expoClient;

    public ExpoNotificationServiceImpl(BotService botService, ExpoClient expoClient) {
        this.botService = botService;
        this.expoClient = expoClient;
    }

    @Override
    public void sendNotification(String hid, String token) {
        try {
            val expoId = botService.pushTokenId(hid, token);
            val dto = new ExpoPushDTO(
                expoId,
                "Hedvig",
                "Hej! Hedvig har svarat på din fråga"
            );
            logger.info("Attempting  to send push to user with id: {}, body: {}", hid, dto.toString());
            val result = expoClient.sendPush(dto);
            logger.info("Got result from expo for push notification to user with id: {}, body: {}", hid, result);
        } catch (Exception e) {
            logger.error("Error, could not send push notification through expo", e);
        }
    }
}
