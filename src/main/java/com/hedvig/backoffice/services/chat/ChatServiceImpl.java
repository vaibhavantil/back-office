package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.config.feign.ExternalServiceBadRequestException;
import com.hedvig.backoffice.config.feign.ExternalServiceException;
import com.hedvig.backoffice.domain.ChatContext;
import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.domain.Subscription;
import com.hedvig.backoffice.repository.ChatContextRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.chat.data.Message;
import com.hedvig.backoffice.services.expo.ExpoNotificationService;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.web.dto.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final SimpMessagingTemplate template;

    private final BotService botService;

    private final MemberService memberService;

    private final ChatContextRepository chatContextRepository;

    private final PersonnelService personnelService;

    private final ExpoNotificationService expoNotificationService;

    private final SubscriptionService subscriptionService;

    public ChatServiceImpl(
            SimpMessagingTemplate template,
            BotService botService,
            MemberService memberService,
            ChatContextRepository chatContextRepository,
            PersonnelService personnelService,
            ExpoNotificationService expoNotificationService,
            SubscriptionService subscriptionService
    ) {

        this.template = template;
        this.botService = botService;
        this.memberService = memberService;
        this.chatContextRepository = chatContextRepository;
        this.personnelService = personnelService;
        this.expoNotificationService = expoNotificationService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void send(String hid, String personnelId, Message message) {
        template.convertAndSendToUser(personnelId, getTopicPrefix() + hid, message.toJson());
    }

    @Override
    public void append(String hid, String message, String personnelId, String token) {
        try {
            botService.response(hid, message, token);
            expoNotificationService.sendNotification(hid, token);
        } catch (ExternalServiceBadRequestException e) {
            send(hid, personnelId, Message.error(400, e.getMessage()));
            log.error("chat not updated hid = " + hid, e);
        }
    }

    @Override
    public void messages(String hid, String personnelId, String token) {
        try {
            send(hid, personnelId, Message.chat(botService.messages(hid, token)));
        } catch (ExternalServiceBadRequestException e) {
            send(hid, personnelId, Message.error(400, e.getMessage()));
            log.error("chat not updated hid = " + hid, e);
        } catch (ExternalServiceException e) {
            send(hid, personnelId, Message.error(500, e.getMessage()));
            log.error("can't fetch member hid = " + hid, e);
        }
    }

    @Override
    public void messages(String hid, int count, String personnelId, String token) {
        try {
            send(hid, personnelId, Message.chat(botService.messages(hid, count, token)));
        } catch (ExternalServiceBadRequestException e) {
            send(hid, personnelId, Message.error(400, e.getMessage()));
            log.error("chat not updated hid = " + hid, e);
        } catch (ExternalServiceException e) {
            send(hid, personnelId, Message.error(500, e.getMessage()));
            log.error("can't fetch member hid = " + hid, e);
        }
    }

    @Override
    @Transactional
    public void close(String sessionId) {
        chatContextRepository.deleteBySessionId(sessionId);
    }

    @Override
    @Transactional
    public void subscribe(String hid, String subId, String sessionId, String principalId) {
        Personnel personnel;
        try {
            personnel = personnelService.getPersonnel(principalId);
        } catch (AuthorizationException e) {
            // TODO
            //send(hid, personnel.getId(), Message.error(400, "Not authorized"));
            log.warn("member not authorized hid = " + hid);
            return;
        }

        MemberDTO member;
        try {
            member = memberService.findByHid(hid, personnelService.getIdToken(personnel));
        } catch (ExternalServiceBadRequestException e) {
            send(hid, personnel.getId(), Message.error(400, "member with hid " + hid + " not found"));
            log.warn("member with hid " + hid + " not found", e);
            return;
        } catch (ExternalServiceException e) {
            send(hid, personnel.getId(), Message.error(500, e.getMessage()));
            log.error("can't fetch member hid = " + hid, e);
            return;
        }

        ChatContext chat = new ChatContext();

        chat.setHid(member.getHid());
        chat.setSubId(subId);
        chat.setSessionId(sessionId);
        chat.setActive(true);
        chat.setTimestamp(new Date().toInstant());
        chat.setPersonnel(personnel);

        Subscription sub = subscriptionService.getOrCreateSubscription(member.getHid());

        chat.setSubscription(sub);
        chatContextRepository.save(chat);
    }

    @Override
    @Transactional
    public void unsubscribe(String subId, String sessionId) {
        chatContextRepository.deleteBySubIdAndSessionId(subId, sessionId);
    }

    @Override
    public String getTopicPrefix() {
        return "/messages/";
    }

}
