package com.hedvig.backoffice.services.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.config.feign.ExternalServiceException;
import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.BackOfficeResponseDTO;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.messages.dto.ExpoPushTokenDTO;
import com.hedvig.backoffice.services.messages.dto.FirebasePushTokenDTO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

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
  public List<BotMessage> messages(String memberId, String token) {
    JsonNode root = botServiceClient.messages(memberId, token);
    return parseMessages(root);
  }

  @Override
  public List<BotMessage> messages(String memberId, int count, String token) {
    JsonNode root = botServiceClient.messages(memberId, count, token);
    return parseMessages(root);
  }

  @Override
  public List<BackOfficeMessage> fetch(Instant timestamp, String token) {
    List<BackOfficeMessage> result = new ArrayList<>();

    botServiceClient.fetch(timestamp.toEpochMilli(), token).forEach(m -> {
      try {
        BotMessage bm = m.toBotMessage();
        if (!bm.isEmptyBody() && bm.getFromId() == 1) result.add(m);
      } catch (BotMessageException e) { log.error(e.toString());}
    });

    return result;
  }

  @Override
  public void response(String memberId, String message, String token) {
    botServiceClient.response(new BackOfficeResponseDTO(memberId, message), token);
  }

  @Override
  public void answerQuestion(String memberId, String answer, String token) {
    botServiceClient.answer(new BackOfficeResponseDTO(memberId, answer), token);
  }

  private List<BotMessage> parseMessages(JsonNode root) {
    if (root == null) {
      throw new ExternalServiceException("bot-service internal error or service unavailable");
    }

    Iterable<Map.Entry<String, JsonNode>> iterable = root::fields;

    return StreamSupport.stream(iterable.spliterator(), false)
        .map(
            e -> {
              try {
                BotMessage bm = new BotMessage(e.getValue().toString());
                return bm.isEmptyBody() ? null : bm;
              } catch (BotMessageException ex) {
                log.error(ex.getMessage(), ex);
                return null;
              }
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
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
