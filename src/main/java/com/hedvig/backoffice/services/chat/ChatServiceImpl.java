package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.domain.ChatContext;
import com.hedvig.backoffice.repository.ChatContextRepository;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceException;
import com.hedvig.backoffice.services.messages.data.ErrorMessage;
import com.hedvig.backoffice.services.messages.data.Message;
import com.hedvig.backoffice.services.users.UserNotFoundException;
import com.hedvig.backoffice.services.users.UserService;
import com.hedvig.backoffice.web.dto.UserDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final SimpMessagingTemplate template;
    private final BotService botService;
    private final UserService userService;
    private final ChatContextRepository chatContextRepository;

    public ChatServiceImpl(SimpMessagingTemplate template, BotService botService, UserService userService,
                           ChatContextRepository chatContextRepository) {
        this.template = template;
        this.botService = botService;
        this.userService = userService;
        this.chatContextRepository = chatContextRepository;
    }

    @Override
    public void retranslate(String hid, Message message) {
        template.convertAndSend(getTopicPrefix() + hid, message.getPayload());
    }

    @Override
    public void append(String hid, Message message) {
        UserDTO user;
        try {
            user = userService.findByHid(hid);
        } catch (UserNotFoundException e) {
            retranslate(hid, new ErrorMessage(404, "User with hid " + hid + " not found"));
            return;
        }

        try {
            botService.response(user.getHid(), message);
        } catch (BotServiceException e) {
            retranslate(hid, new ErrorMessage(500, e.getMessage()));
            return;
        }
        retranslate(hid, message);
    }

    @Override
    public void close(String sessionId) {
        List<ChatContext> chats = chatContextRepository.findBySessionId(sessionId);
        chatContextRepository.delete(chats);
    }

    @Override
    public void subscribe(String hid, String subId, String sessionId) {
        UserDTO user;
        try {
            user = userService.findByHid(hid);
        } catch (UserNotFoundException e) {
            retranslate(hid, new ErrorMessage(404, "User with hid " + hid + " not found"));
            return;
        }

        ChatContext chat = new ChatContext();
        chat.setHid(user.getHid());
        chat.setSubId(subId);
        chat.setSessionId(sessionId);
        chatContextRepository.save(chat);
    }

    @Override
    public void unsubscribe(String subId, String sessionId) {
        chatContextRepository.findBySubIdAndSessionId(subId, sessionId).ifPresent(chatContextRepository::delete);
    }

    @Override
    public String getTopicPrefix() {
        return "/topic/messages/";
    }

}
