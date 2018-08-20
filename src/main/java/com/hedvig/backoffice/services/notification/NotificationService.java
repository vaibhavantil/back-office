package com.hedvig.backoffice.services.notification;

public interface NotificationService {
  void sendPushNotification(String memberId, String personnelToken);
}
