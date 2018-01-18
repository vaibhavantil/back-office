package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.domain.ChatContext;
import com.hedvig.backoffice.repository.ChatContextRepository;
import com.hedvig.backoffice.services.chat.data.Message;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceException;
import com.hedvig.backoffice.services.messages.BotServiceMessage;
import com.hedvig.backoffice.services.users.UserNotFoundException;
import com.hedvig.backoffice.services.users.UserService;
import com.hedvig.backoffice.services.users.UserServiceException;
import com.hedvig.backoffice.web.dto.UserDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public void send(String hid, Message message) {
        template.convertAndSend(getTopicPrefix() + hid, message.toJson());
    }

    @Override
    public void append(String hid, String message) {
        Optional<ChatContext> chatOptional = chatContextRepository.finByHid(hid);
        if (!chatOptional.isPresent()) {
            send(hid, Message.error(400, "User with hid " + hid + " not found"));
            return;
        }

        try {
            botService.response(hid, new BotServiceMessage(message));
        } catch (BotServiceException e) {
            send(hid, Message.error(500, e.getMessage()));
            return;
        }
    }

    @Override
    public void messages(String hid) {
        try {
            send(hid, Message.chat(botService.messages(hid)));
        } catch (BotServiceException e) {
            send(hid, Message.error(500, e.getMessage()));
        }
    }

    @Override
    public void messages(String hid, int count) {
        try {
            send(hid, Message.chat(botService.messages(hid, count)));
        } catch (BotServiceException e) {
            send(hid, Message.error(500, e.getMessage()));
        }
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
            send(hid, Message.error(400, "User with hid " + hid + " not found"));
            return;
        } catch (UserServiceException e) {
            send(hid, Message.error(500, e.getMessage()));
            return;
        }

        Optional<ChatContext> optional = chatContextRepository.finByHid(user.getHid());
        ChatContext chat = optional.orElseGet(ChatContext::new);

        chat.setHid(user.getHid());
        chat.setSubId(subId);
        chat.setSessionId(sessionId);
        chat.setTimestamp(new Date().toInstant());

        chatContextRepository.save(chat);
    }

    @Override
    public void unsubscribe(String subId, String sessionId) {
        Optional<ChatContext> optional = chatContextRepository.findBySubIdAndSessionId(subId, sessionId);
        optional.ifPresent(chatContextRepository::delete);
    }

    @Override
    public String getTopicPrefix() {
        return "/topic/messages/";
    }

}
