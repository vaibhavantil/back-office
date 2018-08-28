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
import com.hedvig.backoffice.services.notificationService.NotificationService;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.web.dto.MemberDTO;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  private final NotificationService notificationService;

  public ChatServiceImpl(
      SimpMessagingTemplate template,
      BotService botService,
      MemberService memberService,
      ChatContextRepository chatContextRepository,
      PersonnelService personnelService,
      ExpoNotificationService expoNotificationService,
      SubscriptionService subscriptionService,
      NotificationService notificationService) {

    this.template = template;
    this.botService = botService;
    this.memberService = memberService;
    this.chatContextRepository = chatContextRepository;
    this.personnelService = personnelService;
    this.expoNotificationService = expoNotificationService;
    this.subscriptionService = subscriptionService;
    this.notificationService = notificationService;
  }

  @Override
  public void send(String memberId, String personnelId, Message message) {
    template.convertAndSendToUser(personnelId, getTopicPrefix() + memberId, message.toJson());
  }

  @Override
  public void append(String memberId, String message, String personnelId, String token) {
    try {
      botService.response(memberId, message, token);
      sendNotification(memberId, token);
    } catch (ExternalServiceBadRequestException e) {
      send(memberId, personnelId, Message.error(400, e.getMessage()));
      log.error("chat not updated memberId = " + memberId, e);
    } catch (ExternalServiceException e) {
      send(memberId, personnelId, Message.error(500, e.getMessage()));
      log.error("chat not updated memberId = " + memberId, e);
    }
  }

  @Override
  public void messages(String memberId, String personnelId, String token) {
    try {
      send(memberId, personnelId, Message.chat(botService.messages(memberId, token)));
    } catch (ExternalServiceBadRequestException e) {
      send(memberId, personnelId, Message.error(400, e.getMessage()));
      log.error("chat not updated memberId = " + memberId, e);
    } catch (ExternalServiceException e) {
      send(memberId, personnelId, Message.error(500, e.getMessage()));
      log.error("can't fetch member memberId = " + memberId, e);
    }
  }

  @Override
  public void messages(String memberId, int count, String personnelId, String token) {
    try {
      send(memberId, personnelId, Message.chat(botService.messages(memberId, count, token)));
    } catch (ExternalServiceBadRequestException e) {
      send(memberId, personnelId, Message.error(400, e.getMessage()));
      log.error("chat not updated memberId = " + memberId, e);
    } catch (ExternalServiceException e) {
      send(memberId, personnelId, Message.error(500, e.getMessage()));
      log.error("can't fetch member memberId = " + memberId, e);
    }
  }

  @Override
  @Transactional
  public void close(String sessionId) {
    chatContextRepository.deleteBySessionId(sessionId);
  }

  @Override
  @Transactional
  public void subscribe(String memberId, String subId, String sessionId, String principalId) {
    Personnel personnel;
    try {
      personnel = personnelService.getPersonnel(principalId);
    } catch (AuthorizationException e) {
      send(memberId, principalId, Message.error(400, "Not authorized"));
      log.warn("member not authorized memberId = " + memberId);
      return;
    }

    MemberDTO member;
    try {
      member = memberService.findByMemberId(memberId, personnelService.getIdToken(personnel));
    } catch (ExternalServiceBadRequestException e) {
      send(
          memberId,
          personnel.getId(),
          Message.error(400, "member with memberId " + memberId + " not found"));
      log.warn("member with memberId " + memberId + " not found", e);
      return;
    }

    if (member == null) {
      send(memberId, personnel.getId(), Message.error(500, "member service unavailable"));
      log.error("can't fetch member memberId = " + memberId);
      return;
    }

    ChatContext chat = new ChatContext();

    chat.setMemberId(member.getMemberId().toString());
    chat.setSubId(subId);
    chat.setSessionId(sessionId);
    chat.setActive(true);
    chat.setTimestamp(new Date().toInstant());
    chat.setPersonnel(personnel);

    Subscription sub = subscriptionService.getOrCreateSubscription(member.getMemberId().toString());

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

  private void sendNotification(String memberId, String personnelToken) {
    if (notificationService.getFirebaseToken(memberId).isPresent()) {
      notificationService.sendPushNotification(memberId);
      return;
    }

    expoNotificationService.sendNotification(memberId, personnelToken);
  }
}
