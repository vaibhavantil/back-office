package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.messages.dto.BackOfficeAnswerDTO;
import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.PushTokenDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        name = "bot-service",
        url = "${botservice.baseUrl}",
        configuration = FeignConfig.class,
        fallback = BotServiceClientFallback.class)
public interface BotServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/_/member/{hid}/push-token")
    PushTokenDTO getPushTokenByHid(@PathVariable("hid") String hid);

    @RequestMapping(method = RequestMethod.POST, value = "/_/messages/addmessage")
    void response(@RequestBody BackOfficeMessage message);

    @RequestMapping(method = RequestMethod.POST, value = "/_/messages/addanswer")
    void answer(@RequestBody BackOfficeAnswerDTO answer);
}
