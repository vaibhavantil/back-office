package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.services.members.dto.ChatResponseMailDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="member-service", url="http://member-service")
public interface MemberServiceClient {
    @RequestMapping(method = RequestMethod.POST, value="/_/mail/sendChatResponseEmail")
    void sendChatResponseEmail(ChatResponseMailDTO mail);
}
