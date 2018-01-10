package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.services.messages.data.Message;
import com.hedvig.backoffice.services.messages.MessageService;
import com.hedvig.backoffice.services.users.UserNotFoundException;
import com.hedvig.backoffice.services.users.UserService;
import com.hedvig.backoffice.web.dto.UserDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    private final SimpMessagingTemplate template;
    private final MessageService messageService;
    private final UserService userService;

    public ChatServiceImpl(SimpMessagingTemplate template, MessageService messageService, UserService userService) {
        this.template = template;
        this.messageService = messageService;
        this.userService = userService;
    }

    @Override
    public void retranslate(String hid, Message message) throws UserNotFoundException {
        UserDTO user = userService.findByHid(hid);
        messageService.response(user.getHid(), message);
        template.convertAndSend(getTopicPrefix() + hid, message);
    }

    @Override
    public void close(String sessionId) {

    }

    @Override
    public void subscribe(String hid, String subId, String sessionId) {

    }

    @Override
    public void unsubscribe(String subId, String sessionId) {

    }


    @Override
    public String getTopicPrefix() {
        return "/topic/messages/";
    }

}
