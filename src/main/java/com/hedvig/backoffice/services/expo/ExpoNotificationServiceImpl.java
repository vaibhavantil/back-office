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
    public void sendNotification(String hid) {
        val expoId = botService.pushTokenId(hid);
        val dto = new ExpoPushDTO(
            expoId,
            "Hedvig",
            "Hej! Hedvig har svarat på din fråga"
        );
        expoClient.sendPush(dto);
    }
}
