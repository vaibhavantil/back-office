package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.config.feign.ExternalServiceBadRequestException;
import com.hedvig.backoffice.config.feign.ExternalServiceException;
import com.hedvig.backoffice.domain.ChatContext;
import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.domain.Subscription;
import com.hedvig.backoffice.repository.ChatContextRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.UploadedFilePostprocessor;
import com.hedvig.backoffice.services.chat.data.Message;
import com.hedvig.backoffice.services.expo.ExpoNotificationService;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.members.dto.MemberDTO;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.dto.BotMessageDTO;
import com.hedvig.backoffice.services.notificationService.NotificationService;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Deprecated
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

  private final SimpMessagingTemplate template;

  private final BotService botService;

  private final MemberService memberService;

  private final ChatContextRepository chatContextRepository;

  private final PersonnelService personnelService;

  private final SubscriptionService subscriptionService;

  private final UploadedFilePostprocessor messagePostProcessor;

  private final ChatServiceV2 chatServiceV2;

  public ChatServiceImpl(
    SimpMessagingTemplate simpMessagingTemplate,
    BotService botService,
    MemberService memberService,
    ChatContextRepository chatContextRepository,
    PersonnelService personnelService,
    SubscriptionService subscriptionService,
    UploadedFilePostprocessor messagePostProcessor,
    ChatServiceV2 chatServiceV2) {

    this.template = simpMessagingTemplate;
    this.botService = botService;
    this.memberService = memberService;
    this.chatContextRepository = chatContextRepository;
    this.personnelService = personnelService;
    this.subscriptionService = subscriptionService;
    this.messagePostProcessor = messagePostProcessor;
    this.chatServiceV2 = chatServiceV2;
  }

  @Override
  public void send(String memberId, String personnelEmail, Message message) {
    template.convertAndSendToUser(personnelEmail, getTopicPrefix() + memberId, message.toJson());
  }

  @Override
  public void append(String memberId, String message, boolean forceSendMessage, String personnelId, String token) {
    try {
      botService.response(memberId, message, forceSendMessage, token);
      chatServiceV2.sendNotification(memberId, token);
    } catch (ExternalServiceBadRequestException e) {
      send(memberId, personnelId, Message.error(400, e.getMessage()));
      log.error("chat not updated memberId = " + memberId, e);
    } catch (ExternalServiceException e) {
      send(memberId, personnelId, Message.error(500, e.getMessage()));
      log.error("chat not updated memberId = " + memberId, e);
    }
  }

  @Override
  public void messages(String memberId, String personnelEmail, String token) {
    try {
      List<BotMessageDTO> messages = botService.messages(memberId, token);
      messages.forEach(messagePostProcessor::processMessage);
      send(memberId, personnelEmail, Message.chat(messages));
    } catch (ExternalServiceBadRequestException e) {
      send(memberId, personnelEmail, Message.error(400, e.getMessage()));
      log.error("chat not updated memberId = " + memberId, e);
    } catch (ExternalServiceException e) {
      send(memberId, personnelEmail, Message.error(500, e.getMessage()));
      log.error("can't fetch member memberId = " + memberId, e);
    }
  }

  @Override
  public void messages(String memberId, int count, String personnelEmail, String token) {
    try {
      List<BotMessageDTO> messages = botService.messages(memberId, count, token);
      messages.forEach(messagePostProcessor::processMessage);
      send(memberId, personnelEmail, Message.chat(messages));
    } catch (ExternalServiceBadRequestException e) {
      send(memberId, personnelEmail, Message.error(400, e.getMessage()));
      log.error("chat not updated memberId = " + memberId, e);
    } catch (ExternalServiceException e) {
      send(memberId, personnelEmail, Message.error(500, e.getMessage()));
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
  public void subscribe(String memberId, String subId, String sessionId, String principalEmail) {
    Personnel personnel;
    try {
      personnel = personnelService.getPersonnelByEmail(principalEmail);
    } catch (AuthorizationException e) {
      send(memberId, principalEmail, Message.error(400, "Not authorized"));
      log.warn("member not authorized memberId = " + memberId);
      return;
    }

    MemberDTO member;
    try {
      member = memberService.findByMemberId(memberId, personnelService.getIdToken(personnel.getEmail()));
    } catch (ExternalServiceBadRequestException e) {
      send(
          memberId,
          personnel.getEmail(),
          Message.error(400, "member with memberId " + memberId + " not found"));
      log.warn("member with memberId " + memberId + " not found", e);
      return;
    }

    if (member == null) {
      send(memberId, personnel.getEmail(), Message.error(500, "member service unavailable"));
      log.error("can't fetch member memberId = " + memberId);
      return;
    }

    ChatContext chat = new ChatContext();

    chat.setMemberId(member.getMemberId() + "");
    chat.setSubId(subId);
    chat.setSessionId(sessionId);
    chat.setActive(true);
    chat.setTimestamp(new Date().toInstant());
    chat.setPersonnel(personnel);

    Subscription sub = subscriptionService.getOrCreateSubscription(member.getMemberId() + "");

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
