package com.hedvig.backoffice.services.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.services.messages.dto.*;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BotServiceClientFallback implements BotServiceClient {

  @Override
  public ExpoPushTokenDTO getPushTokenByMemberId(String memberId, String token) {
    log.error("request to bot-service failed");
    return new ExpoPushTokenDTO(null);
  }

  @Override
  public Map<Integer, BotMessageDTO> messages(String memberId, String token) {
    log.error("request to bot-service failed");
    return null;
  }

  @Override
  public Map<Integer, BotMessageDTO> messages(String memberId, int count, String token) {
    log.error("request to bot-service failed");
    return null;
  }

  @Override
  public List<BackOfficeMessage> fetch(long time, String token) {
    log.error("request to bot-service failed");
    return null;
  }

  @Override
  public void response(BackOfficeResponseDTO message, String token) {
    log.error("request to bot-service failed");
  }

  @Override
  public void answer(BackOfficeResponseDTO answer, String token) {
    log.error("request to bot-service failed");
  }

  @Override
  public FirebasePushTokenDTO getFirebasePushToken(String memberId, String token) {
    log.error("request to bot-service failed");
    return null;
  }

  @Override
  public List<FileUploadDTO> getFileUploads(String id, String token) {
    log.error("request to bot-service failed");
    return null;
  }
}
