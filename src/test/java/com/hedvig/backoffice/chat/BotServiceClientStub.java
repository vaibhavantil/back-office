package com.hedvig.backoffice.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.chat.datagenerator.TestDataGenerator;
import com.hedvig.backoffice.services.messages.BotServiceClient;
import com.hedvig.backoffice.services.messages.dto.*;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log
public class BotServiceClientStub implements BotServiceClient {

  @Override
  public ExpoPushTokenDTO getPushTokenByMemberId(String memberId, String token) {
    return null;
  }

  @Override
  public Map<Integer, BotMessageDTO> messages(String memberId, String token) {
    return new TestDataGenerator().getExamplesForBodyChecking(token);
  }

  @Override
  public Map<Integer, BotMessageDTO> messages(String memberId, int count, String token) {
    return new TestDataGenerator().getExamplesForBodyChecking(token);
  }

  @Override
  public List<BackOfficeMessage> fetch(long time, String token) {
    BackOfficeMessage bom;

    bom = new BackOfficeMessage("1", new TestDataGenerator().getExampleForBodyChecking(token));
    List<BackOfficeMessage> res = new ArrayList<>();
    res.add(bom);
    return res;

  }

  @Override
  public void response(BackOfficeResponseDTO message, String token) {

  }

  @Override
  public void answer(BackOfficeResponseDTO answer, String token) {

  }

  @Override
  public FirebasePushTokenDTO getFirebasePushToken(String memberId, String token) {
    return null;
  }

  @Override
  public List<FileUploadDTO> getFileUploads(String memberId, String token) { return null; }
}


