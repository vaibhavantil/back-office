package com.hedvig.backoffice.chat;

import com.hedvig.backoffice.chat.datagenerator.TestDataGenerator;
import com.hedvig.backoffice.services.messages.BotService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes=TestServiceConfig.class)
public class TestChat {
  @Autowired
  private BotService botService;

  @Test
  public void correctMessageTest () {
    List messageList = botService.fetch(Instant.now(), TestDataGenerator.BODY_WITH_URL_AND_TEXT);
    Assert.assertTrue(messageList.size()>0);
  }

  @Test
  public void emptyTextMessageTest () {
    List messageList = botService.fetch(Instant.now(), TestDataGenerator.BODY_WITH_TEXT);
    Assert.assertTrue(messageList.size()>0);
  }

  @Test
  public void emptyUrlMessageTest () {
    List messageList = botService.fetch(Instant.now(), TestDataGenerator.BODY_WITH_URL);
    Assert.assertTrue(messageList.size()>0);
  }

  @Test
  public void emptyBodyMessageTest () {
    List messageList = botService.fetch(Instant.now(), TestDataGenerator.EMPTY_BODY);
    Assert.assertTrue(messageList.size()==0);
  }

  @Test
  public void emptyBodyWithDiferentFromMessageTest () {
    List messageList = botService.fetch(Instant.now(), TestDataGenerator.EMPTY_BODY_WITH_FROMID);
    Assert.assertTrue(messageList.size()>0);
  }

}
