package com.hedvig.backoffice.services.notificationService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "notification-service", url = "${notificationService.baseUrl:notification-service}")
public interface NotificationServiceClient {
  @PostMapping("/_/notifications/{memberId}/push/send")
  ResponseEntity<?> sendPushNotification(@PathVariable(name = "memberId") String memberId);

  @GetMapping("/_/notifications/{memberId}/token")
  ResponseEntity<String> getFirebaseToken(@PathVariable(name = "memberId") String memberId);

  @PostMapping("/_/notifications/{memberId}/token")
  ResponseEntity<?> setFirebaseToken(
      @PathVariable(name = "memberId") String memberId, String token);
}
