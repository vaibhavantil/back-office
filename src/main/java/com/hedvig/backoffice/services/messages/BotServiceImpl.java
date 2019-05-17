package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.BackOfficeResponseDTO;
import com.hedvig.backoffice.services.messages.dto.BotMessageDTO;
import com.hedvig.backoffice.services.messages.dto.ExpoPushTokenDTO;
import com.hedvig.backoffice.services.messages.dto.FirebasePushTokenDTO;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class BotServiceImpl implements BotService {
  private BotServiceClient botServiceClient;

  @Autowired
  private BotServiceImpl(BotServiceClient botServiceClient) {
    this.botServiceClient = botServiceClient;

    log.info("BOT SERVICE:");
    log.info("class: " + BotServiceImpl.class.getName());
  }

  @Override
  public List<BotMessageDTO> messages(String memberId, String token) {
    Map<Integer, BotMessageDTO> root = botServiceClient.messages(memberId, token);
    return root.values().stream()
      .filter(msg -> !msg.isEmptyBody())
      .collect(Collectors.toList());
  }

  @Override
  public List<BotMessageDTO> messages(String memberId, int count, String token) {
    Map<Integer, BotMessageDTO> root = botServiceClient.messages(memberId, count, token);
    return root.values().stream()
      .filter(msg -> !msg.isEmptyBody())
      .collect(Collectors.toList());
  }

  @Override
  public List<BackOfficeMessage> fetch(Instant timestamp, String token) {
    return botServiceClient.fetch(timestamp.toEpochMilli(), token).stream()
      .filter(m -> !m.getMsg().isEmptyBody() || !m.getMsg().isBotMessage())
      .collect(Collectors.toList());
  }

  @Override
  public void response(String memberId, String message, boolean forceSendMessage, String token) {
    botServiceClient.response(new BackOfficeResponseDTO(memberId, message, forceSendMessage), token);
  }

  @Override
  public void answerQuestion(String memberId, String answer, String token) {
    botServiceClient.answer(new BackOfficeResponseDTO(memberId, answer, false), token);
  }

  @Override
  public ExpoPushTokenDTO getExpoPushToken(String memberId, String token) {
    return botServiceClient.getPushTokenByMemberId(memberId, token);
  }

  @Override
  public Optional<FirebasePushTokenDTO> getFirebasePushToken(String memberId, String token) {
    try {
      return Optional.of(botServiceClient.getFirebasePushToken(memberId, token));
    } catch (FeignException e) {
      if (e.status() != 404) {
        log.error("Request to bot-service failed: {}", e);
      }
      return Optional.empty();
    }
  }
}
