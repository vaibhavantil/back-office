package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.domain.ChatContext;
import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.domain.Subscription;
import com.hedvig.backoffice.repository.ChatContextRepository;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.repository.SubscriptionRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {

    private final SimpMessagingTemplate template;
    private final BotService botService;
    private final UserService userService;
    private final ChatContextRepository chatContextRepository;
    private final PersonnelRepository personnelRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MessageUrlResolver messageUrlResolver;

    public ChatServiceImpl(SimpMessagingTemplate template,
                           BotService botService,
                           UserService userService,
                           ChatContextRepository chatContextRepository,
                           PersonnelRepository personnelRepository,
                           SubscriptionRepository subscriptionRepository,
                           MessageUrlResolver messageUrlResolver) {

        this.template = template;
        this.botService = botService;
        this.userService = userService;
        this.chatContextRepository = chatContextRepository;
        this.personnelRepository = personnelRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.messageUrlResolver = messageUrlResolver;
    }

    @Override
    public void send(String hid, Message message) {
        template.convertAndSend(getTopicPrefix() + hid, message.toJson());
    }

    @Override
    public void append(String hid, String message) {
        Optional<ChatContext> chatOptional = chatContextRepository.findByHid(hid);
        if (!chatOptional.isPresent()) {
            send(hid, Message.error(400, "User with hid " + hid + " not found"));
            return;
        }

        try {
            BotServiceMessage msg = new BotServiceMessage(message, true);
            messageUrlResolver.resolveUrls(msg);
            botService.response(hid, msg);
        } catch (BotServiceException e) {
            send(hid, Message.error(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public void messages(String hid) {
        try {
            send(hid, Message.chat(botService.messages(hid)));
        } catch (BotServiceException e) {
            send(hid, Message.error(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public void messages(String hid, int count) {
        try {
            send(hid, Message.chat(botService.messages(hid, count)));
        } catch (BotServiceException e) {
            send(hid, Message.error(e.getCode(), e.getMessage()));
        }
    }

    @Override
    @Transactional
    public void close(String sessionId) {
        List<ChatContext> chats = chatContextRepository.findBySessionId(sessionId);
        chatContextRepository.delete(chats);
    }

    @Override
    @Transactional
    public void subscribe(String hid, String subId, String sessionId, String principal) {
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

        Optional<Personnel> personnelOptional = personnelRepository.findByEmail(principal);
        if (!personnelOptional.isPresent()) {
            send(hid, Message.error(400, "Not authorized"));
            return;
        }

        Personnel personnel = personnelOptional.get();

        Optional<ChatContext> chatOptional = chatContextRepository.findByHidAndPersonnel(user.getHid(), personnel);
        ChatContext chat = chatOptional.orElseGet(ChatContext::new);

        chat.setHid(user.getHid());
        chat.setSubId(subId);
        chat.setSessionId(sessionId);
        chat.setTimestamp(new Date().toInstant());
        chat.setPersonnel(personnel);

        Optional<Subscription> subOptional = subscriptionRepository.finByHid(user.getHid());
        Subscription sub = subOptional.orElseGet(() -> {
            Subscription newSub = new Subscription(user.getHid());
            subscriptionRepository.save(newSub);
            return newSub;
        });

        chat.setSubscription(sub);
        chatContextRepository.save(chat);
    }

    @Override
    @Transactional
    public void unsubscribe(String subId, String sessionId) {
        Optional<ChatContext> optional = chatContextRepository.findBySubIdAndSessionId(subId, sessionId);
        optional.ifPresent(chatContextRepository::delete);
    }

    @Override
    public String getTopicPrefix() {
        return "/topic/messages/";
    }

}
