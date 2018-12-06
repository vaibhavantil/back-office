package com.hedvig.backoffice.chat.datagenerator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hedvig.backoffice.services.messages.dto.BotMessageDTO;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestDataGenerator {

  public static String EMPTY_BODY = "empty_body";
  public static String EMPTY_BODY_WITH_FROMID = "empty_body_with_dif_fromid";
  public static String BODY_WITH_TEXT = "body_width_text";
  public static String BODY_WITH_URL = "body_width_url";
  public static String BODY_WITH_URL_AND_TEXT = "body_width_url_and_text";

  private static final String STUB_MESSAGE_TEMPLATE =
    "{"
      + "\"globalId\": %s,"
      + "\"id\": %s,"
      + "\"header\": { "
      + "   \"messageId\": %s,"
      + "   \"fromId\": %s"
      + "},"
      + "\"body\": {"
      + "   \"type\": \"%s\","
      + "   \"text\": \"%s\","
      + "   \"URL\": \"%s\""
      + "},"
      + "\"timestamp\":\"%s\""
      + "}";

  private ObjectMapper mapper;
  private Map <String, String> strategies;

  public TestDataGenerator() {
    mapper = new ObjectMapper();
    mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    mapper.registerModule(new JavaTimeModule());

    initParams();

  }


  public BotMessageDTO getExampleForBodyChecking (String strategy) {
    try {
      return mapper.readValue(strategies.get(strategy), BotMessageDTO.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public Map<Integer, BotMessageDTO> getExamplesForBodyChecking(String strategy) {
    try {
      BotMessageDTO msg = mapper.readValue(strategies.get(strategy), BotMessageDTO.class);
      HashMap<Integer, BotMessageDTO> res = new HashMap<>();
      res.put(1, msg);
      return res;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  private void initParams () {
    strategies = new HashMap<>();
    String time = new Date().toInstant().toString();
    strategies.put(BODY_WITH_URL_AND_TEXT, String.format(STUB_MESSAGE_TEMPLATE, "1", "1", "1", "1", "test", "text", "URL", time));
    strategies.put(EMPTY_BODY, String.format(STUB_MESSAGE_TEMPLATE, "1", "1", "1", "1", "test", null, null, time));
    strategies.put(EMPTY_BODY_WITH_FROMID, String.format(STUB_MESSAGE_TEMPLATE, "1", "1", "1", "2222", "test", null, null, time));
    strategies.put(BODY_WITH_TEXT, String.format(STUB_MESSAGE_TEMPLATE, "1", "1", "1", "1", "test", "text", null, time));
    strategies.put(BODY_WITH_URL, String.format(STUB_MESSAGE_TEMPLATE, "1", "1", "1", "1", "test", null, "URL", time));
  }


}
