package com.hedvig.backoffice.services.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.messages.dto.BackOfficeAnswerDTO;
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

    @GetMapping("/_/member/{hid}/push-token")
    PushTokenDTO getPushTokenByHid(@PathVariable("hid") String hid);

    @GetMapping("/messages")
    JsonNode messages(@RequestHeader("hedvig.token") String hid);

    @GetMapping("/messages/{count}")
    JsonNode messages(@RequestHeader("hedvig.token") String hid, @PathVariable("count") int count);

    @GetMapping("/_/messages/{time}")
    List<BackOfficeMessage> fetch(@PathVariable("time") long time);

    @PostMapping("/_/messages/addmessage")
    void response(@RequestBody BackOfficeMessage message);

    @PostMapping("/_/messages/addanswer")
    void answer(@RequestBody BackOfficeAnswerDTO answer);
}
