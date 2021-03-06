package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.repository.SubscriptionRepository;
import com.hedvig.backoffice.services.messages.dto.*;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class BotServiceStub implements BotService {

  private static final String STUB_MESSAGE_TEMPLATE =
    "{"
      + "\"globalId\": %s,"
      + "\"id\": \"mock.id\","
      + "\"header\": { "
      + "   \"messageId\": %s,"
      + "   \"fromId\": %s"
      + "},"
      + "\"body\": {"
      + "   \"type\": \"%s\","
      + "   \"text\": \"%s\""
      + "   %s"
      + "},"
      + "\"timestamp\":\"%s\""
      + "}";

  private static List<String> typeNames =
    Arrays.asList(
      "text",
      "number",
      "single_select",
      "multiple_select",
      "date_picker",
      "audio",
      "photo_upload",
      "video",
      "hero",
      "paragraph",
      "bankid_collect");
  private static Map<String, String> typesTemplates = new HashMap<>();

  static {
    typesTemplates.put("text", "");
    typesTemplates.put("number", "");

    typesTemplates.put(
      "single_select",
      ",  \"choices\":["
        + "        {"
        + "           \"type\": \"selection\","
        + "           \"text\":\"Jag vill ha en ny\","
        + "           \"selected\": false"
        + "        },{"
        + "           \"type\": \"link\","
        + "           \"text\":\"I want to see my assets\","
        + "           \"webUrl\": \"http://hedvig.com\","
        + "           \"selected\": true"
        + "        }"
        + "     ]");

    typesTemplates.put(
      "multiple_select",
      ",  \"choices\":["
        + "        {"
        + "           \"type\": \"selection\","
        + "           \"text\":\"Jag vill ha en ny\","
        + "           \"selected\": false"
        + "        },{"
        + "           \"type\": \"link\","
        + "           \"text\":\"I want to see my assets\","
        + "           \"webUrl\": \"http://hedvig.com\","
        + "           \"selected\": true"
        + "        }"
        + "     ]");

    typesTemplates.put("date_picker", ", \"date\": [2002,8,25,0,0]");

    typesTemplates.put(
      "audio", ", \"URL\": \"http://78.media.tumblr.com/tumblr_ll313eVnI91qjahcpo1_1280.jpg\"");
    typesTemplates.put(
      "photo_upload",
      ", \"URL\": \"http://78.media.tumblr.com/tumblr_ll313eVnI91qjahcpo1_1280.jpg\"");
    typesTemplates.put("video", ", \"URL\": \"https://media.w3.org/2010/05/sintel/trailer.mp4\"");

    typesTemplates.put(
      "hero",
      ", \"imageUri\": \"http://78.media.tumblr.com/tumblr_ll313eVnI91qjahcpo1_1280.jpg\"");

    typesTemplates.put("paragraph", "");

    typesTemplates.put("bankid_collect", ", \"referenceId\": \"811228-9874\"");
  }

  private final SubscriptionRepository subscriptionRepository;

  private ConcurrentHashMap<String, List<BotMessageDTO>> messages;
  private ConcurrentHashMap<String, Instant> timestamps;
  private AtomicLong increment;

  @Autowired
  public BotServiceStub(SubscriptionRepository subscriptionRepository) {
    this.messages = new ConcurrentHashMap<>();
    increment = new AtomicLong();
    increment.set(0);

    timestamps = new ConcurrentHashMap<>();

    this.subscriptionRepository = subscriptionRepository;

    log.info("BOT SERVICE:");
    log.info("class: " + BotServiceStub.class.getName());
  }

  @Override
  public List<BotMessageDTO> messages(String memberId, String token) {
    List<BotMessageDTO> current = messages.computeIfAbsent(memberId, k -> new ArrayList<>());
    Instant time = new Date().toInstant();
    Instant timestamp = timestamps.computeIfAbsent(memberId, k -> new Date().toInstant());

    if (time.minusSeconds(5).isAfter(timestamp)) {
      timestamps.put(memberId, new Date().toInstant());
      String type = typeNames.get(current.size() % typeNames.size());

      try {
        current.add(
          BotMessageDTO.fromJson(String.format(
              STUB_MESSAGE_TEMPLATE,
              increment.addAndGet(1),
              current.size(),
              memberId,
              type,
              "Test message " + current.size(),
              typesTemplates.get(type),
              time.toString()
            )));
      } catch (Exception e) {
        log.error("error creating message", e);
      }
    }

    return current;
  }

  @Override
  public List<BotMessageDTO> messages(String memberId, int count, String token) {
    List<BotMessageDTO> all = messages(memberId, token);
    if (all.size() <= count) {
      return all;
    }

    return all.subList(all.size() - count, all.size());
  }

  @Override
  public List<BackOfficeMessage> fetch(Instant timestamp, String token) {
    return messages
      .entrySet()
      .stream()
      .flatMap(
        e -> {
          String memberId = e.getKey();
          List<BotMessageDTO> messages = e.getValue();
          return messages
            .stream()
            .filter(Objects::nonNull)
            .filter(m -> m.getTimestamp().isAfter(timestamp))
            .map(m -> new BackOfficeMessage(memberId, m));
        })
      .collect(Collectors.toList());
  }

  @Override
  public void response(String memberId, String message, boolean forceSendMessage, String token) {
    answerQuestion(memberId, message, token);
  }

  @Override
  public void answerQuestion(String memberId, String answer, String token) {
    List<BotMessageDTO> current = messages.computeIfAbsent(memberId, k -> new ArrayList<>());
    try {
      appendMessage(
        memberId,
        BotMessageDTO.fromJson(String.format(
            STUB_MESSAGE_TEMPLATE,
            increment.addAndGet(1),
            current.size(),
            "1",
            "text",
            answer,
            typesTemplates.get("text"),
            new Date().toInstant())));
    } catch (Exception e) {
      log.error("message not created", e);
    }
  }

  private void appendMessage(String memberId, BotMessageDTO bm) {
    List<BotMessageDTO> msg = messages.computeIfAbsent(memberId, k -> new ArrayList<>());
    bm.setGlobalId(increment.addAndGet(1));
    bm.getHeader().setMessageId((long)msg.size());
    msg.add(bm);
  }

  @Scheduled(fixedDelay = 1000)
  public void addMessage() {
    subscriptionRepository.findActiveSubscriptions().forEach(s -> messages(s.getMemberId(), ""));
  }

  @Override
  public ExpoPushTokenDTO getExpoPushToken(String memberId, String token) {
    return new ExpoPushTokenDTO(null);
  }

  @Override
  public Optional<FirebasePushTokenDTO> getFirebasePushToken(String memberId, String token) {
    return Optional.of(new FirebasePushTokenDTO(null));
  }

  @Override
  public List<FileUploadDTO> files(String memberId, String token) {
    List<FileUploadDTO> fileUploads = new ArrayList<>();
    fileUploads.add(new FileUploadDTO(
      "ae4e0980-f3ff-11e8-8f52-37917eb5f47f-img_20181112_193429.jpg",
      Instant.now(),
      "image/jpg",
      memberId
    ));
    fileUploads.add(new FileUploadDTO(
      "a2aae820-f321-11e8-bd6f-670ad6bb9d88-img_20181124_183848.jpg",
      Instant.now(),
      "image/jpg",
      memberId
    ));
    return fileUploads;
  }
}
