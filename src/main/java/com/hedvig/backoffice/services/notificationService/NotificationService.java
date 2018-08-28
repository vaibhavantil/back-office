package com.hedvig.backoffice.services.notificationService;

import java.util.Optional;

public interface NotificationService {

  void sendPushNotification(String memberId);

  Optional<String> getFirebaseToken(String memberId);

  void setFirebaseToken(String memberId, String token);
}
