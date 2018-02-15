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
import com.hedvig.backoffice.services.messages.data.BotServiceMessage;
import com.hedvig.backoffice.services.members.MemberNotFoundException;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.members.MemberServiceException;
import com.hedvig.backoffice.web.dto.MemberDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {

    private static Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    private final SimpMessagingTemplate template;
    private final BotService botService;
    private final MemberService memberService;
    private final ChatContextRepository chatContextRepository;
    private final PersonnelRepository personnelRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MessageUrlResolver messageUrlResolver;

    public ChatServiceImpl(SimpMessagingTemplate template,
                           BotService botService,
                           MemberService memberService,
                           ChatContextRepository chatContextRepository,
                           PersonnelRepository personnelRepository,
                           SubscriptionRepository subscriptionRepository,
                           MessageUrlResolver messageUrlResolver) {

        this.template = template;
        this.botService = botService;
        this.memberService = memberService;
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
        Optional<Subscription> subscriptionOptional = subscriptionRepository.findByHid(hid);
        if (!subscriptionOptional.isPresent()) {
            send(hid, Message.error(400, "member with hid " + hid + " not found"));
            logger.warn("member with hid " + hid + " not found");
            return;
        }

        try {
            BotServiceMessage msg = new BotServiceMessage(message, true);
            messageUrlResolver.resolveUrls(msg);
            botService.response(hid, msg);
        } catch (BotServiceException e) {
            send(hid, Message.error(e.getCode(), e.getMessage()));
            logger.error("chat not updated hid = " + hid, e);
        }
    }

    @Override
    public void messages(String hid) {
        try {
            send(hid, Message.chat(botService.messages(hid)));
        } catch (BotServiceException e) {
            send(hid, Message.error(e.getCode(), e.getMessage()));
            logger.error("chat not updated hid = " + hid, e);
        }
    }

    @Override
    public void messages(String hid, int count) {
        try {
            send(hid, Message.chat(botService.messages(hid, count)));
        } catch (BotServiceException e) {
            send(hid, Message.error(e.getCode(), e.getMessage()));
            logger.error("chat not updated hid = " + hid, e);
        }
    }

    @Override
    @Transactional
    public void close(String sessionId) {
        List<ChatContext> chats = chatContextRepository.findBySessionId(sessionId);
        if (chats.size() > 0) {
            chats.forEach(c -> c.setActive(false));
            chatContextRepository.save(chats);
        }
    }

    @Override
    @Transactional
    public void subscribe(String hid, String subId, String sessionId, String principal) {
        MemberDTO member;
        try {
            member = memberService.findByHid(hid).orElseThrow(MemberServiceException::new);
        } catch (MemberNotFoundException e) {
            send(hid, Message.error(400, "member with hid " + hid + " not found"));
            logger.warn("member with hid " + hid + " not found", e);
            return;
        } catch (MemberServiceException e) {
            send(hid, Message.error(500, e.getMessage()));
            logger.error("can't fetch member hid = " + hid, e);
            return;
        }

        Optional<Personnel> personnelOptional = personnelRepository.findByEmail(principal);
        if (!personnelOptional.isPresent()) {
            send(hid, Message.error(400, "Not authorized"));
            logger.warn("member not authorized hid = " + hid);
            return;
        }

        Personnel personnel = personnelOptional.get();

        Optional<ChatContext> chatOptional = chatContextRepository.findByHidAndPersonnel(member.getHid(), personnel);
        ChatContext chat = chatOptional.orElseGet(ChatContext::new);

        chat.setHid(member.getHid());
        chat.setSubId(subId);
        chat.setSessionId(sessionId);
        chat.setActive(true);
        chat.setTimestamp(new Date().toInstant());
        chat.setPersonnel(personnel);

        Optional<Subscription> subOptional = subscriptionRepository.findByHid(member.getHid());
        Subscription sub = subOptional.orElseGet(() -> {
            Subscription newSub = new Subscription(member.getHid());
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
        optional.ifPresent(c -> {
            c.setActive(false);
            chatContextRepository.save(c);
        });
    }

    @Override
    public String getTopicPrefix() {
        return "/topic/messages/";
    }

}
