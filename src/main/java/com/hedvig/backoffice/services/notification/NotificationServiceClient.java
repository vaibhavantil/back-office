package com.hedvig.backoffice.services.notification;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.notification.dto.SendPushNotificationRequest;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
  name = "notification-service",
  url = "${memberService.baseUrl}",
  configuration = FeignConfig.class
)
public interface NotificationServiceClient {
  @PostMapping("/_/member/{memberId}/notifications/pushNotification")
  void sendPush(@PathVariable("memberId") String memberId, @RequestBody SendPushNotificationRequest sendPushNotificationRequest);
}
