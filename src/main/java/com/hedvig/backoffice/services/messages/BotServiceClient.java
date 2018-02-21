package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.services.messages.data.PushTokenDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "bot-service", url="http://bot-service")
public interface BotServiceClient {
    @RequestMapping(method = RequestMethod.GET, value="/_/member/{hid}/push-token")
    PushTokenDTO getPushTokenByHid(@PathVariable("hid") String hid);
}
