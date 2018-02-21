package com.hedvig.backoffice.services.expo;

import com.hedvig.backoffice.services.messages.BotService;
import lombok.val;

public class ExpoNotificationServiceImpl implements ExpoNotificationService {

    private final BotService botService;
    private final ExpoClient expoClient;

    public ExpoNotificationServiceImpl(BotService botService, ExpoClient expoClient) {
        this.botService = botService;
        this.expoClient = expoClient;
    }

    @Override
    public void sendNotification() {
        // TODO: Get expoId and name from botService
        val expoId = "lqIPV0KLOaE9M0uqHUIPty";
        val name = "Oscar";
        val dto = new ExpoPushDTO(
            String.format("ExponentPushToken[%s]", expoId),
            "Hedvig",
            String.format("Hej %s! Hedvig har svarat på din fråga", name)
        );
        expoClient.sendPush(dto);
    }
}
