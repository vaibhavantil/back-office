package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.domain.ChatContext;
import com.hedvig.backoffice.repository.ChatContextRepository;
import com.hedvig.backoffice.services.messages.MessageService;
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
    private final MessageService messageService;
    private final UserService userService;
    private final ChatContextRepository chatContextRepository;

    public ChatServiceImpl(SimpMessagingTemplate template, MessageService messageService, UserService userService,
                           ChatContextRepository chatContextRepository) {
        this.template = template;
        this.messageService = messageService;
        this.userService = userService;
        this.chatContextRepository = chatContextRepository;
    }

    @Override
    public void retranslate(String hid, Message message) {
        template.convertAndSend(getTopicPrefix() + hid, message);
    }

    @Override
    public void append(String hid, Message message) throws UserNotFoundException {
        UserDTO user = userService.findByHid(hid);
        messageService.response(user.getHid(), message);
        retranslate(hid, message);
    }

    @Override
    public void close(String sessionId) {
        List<ChatContext> chats = chatContextRepository.findBySessionId(sessionId);
        chatContextRepository.delete(chats);
    }

    @Override
    public void subscribe(String hid, String subId, String sessionId) {
        ChatContext chat = new ChatContext();
        chat.setHid(hid);
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
