package com.hedvig.backoffice.services.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.BackOfficeResponseDTO;
import com.hedvig.backoffice.services.messages.dto.ExpoPushTokenDTO;
import com.hedvig.backoffice.services.messages.dto.FirebasePushTokenDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
    name = "bot-service",
    url = "${botservice.baseUrl}",
    configuration = FeignConfig.class,
    fallback = BotServiceClientFallback.class)
public interface BotServiceClient {

  @GetMapping("/_/member/{memberId}/push-token")
  ExpoPushTokenDTO getPushTokenByMemberId(
      @PathVariable("memberId") String memberId, @RequestHeader("Authorization") String token);

  @GetMapping("/messages")
  JsonNode messages(
      @RequestHeader("hedvig.token") String memberId, @RequestHeader("Authorization") String token);

  @GetMapping("/messages/{count}")
  JsonNode messages(
      @RequestHeader("hedvig.token") String memberId,
      @PathVariable("count") int count,
      @RequestHeader("Authorization") String token);

  @GetMapping("/_/messages/{time}")
  List<BackOfficeMessage> fetch(
      @PathVariable("time") long time, @RequestHeader("Authorization") String token);

  @PostMapping("/_/messages/addmessage")
  void response(
      @RequestBody BackOfficeResponseDTO message, @RequestHeader("Authorization") String token);

  @PostMapping("/_/messages/addanswer")
  void answer(
      @RequestBody BackOfficeResponseDTO answer, @RequestHeader("Authorization") String token);

  @GetMapping("/_/v2/{memberId}/push-token")
  FirebasePushTokenDTO getFirebasePushToken(@PathVariable("memberId") String memberId, @RequestHeader("Authorization") String token);
}
