package com.hedvig.backoffice.services.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.messages.dto.BackOfficeResponseDTO;
import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.PushTokenDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "bot-service",
        url = "${botservice.baseUrl}",
        configuration = FeignConfig.class,
        fallback = BotServiceClientFallback.class)
public interface BotServiceClient {

    @GetMapping("/_/member/{memberId}/push-token")
    PushTokenDTO getPushTokenByMemberId(@PathVariable("memberId") String memberId, @RequestHeader("Authorization") String token);

    @GetMapping("/messages")
    JsonNode messages(@RequestHeader("hedvig.token") String memberId, @RequestHeader("Authorization") String token);

    @GetMapping("/messages/{count}")
    JsonNode messages(@RequestHeader("hedvig.token") String memberId, @PathVariable("count") int count, @RequestHeader("Authorization") String token);

    @GetMapping("/_/messages/{time}")
    List<BackOfficeMessage> fetch(@PathVariable("time") long time, @RequestHeader("Authorization") String token);

    @PostMapping("/_/messages/addmessage")
    void response(@RequestBody BackOfficeResponseDTO message, @RequestHeader("Authorization") String token);

    @PostMapping("/_/messages/addanswer")
    void answer(@RequestBody BackOfficeResponseDTO answer, @RequestHeader("Authorization") String token);
}
