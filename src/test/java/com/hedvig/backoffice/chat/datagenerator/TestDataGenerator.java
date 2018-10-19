package com.hedvig.backoffice.chat.datagenerator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log
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

    initParams();

  }


  public JsonNode getExampleForBodyChecking (String strategy) {
    try {
      log.info(strategy+" "+mapper.readValue(strategies.get(strategy), JsonNode.class).toString());
      return mapper.readValue(strategies.get(strategy), JsonNode.class);
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
