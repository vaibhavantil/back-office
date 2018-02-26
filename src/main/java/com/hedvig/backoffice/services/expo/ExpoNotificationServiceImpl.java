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
    public void sendNotification(String hid) {
        try {
            val expoId = botService.pushTokenId(hid);
            val dto = new ExpoPushDTO(
                expoId,
                "Hedvig",
                "Hej! Hedvig har svarat på din fråga"
            );
            expoClient.sendPush(dto);
        } catch (Exception e) {
            logger.error("Error, could not send push notification through expo", e);
        }
    }
}
