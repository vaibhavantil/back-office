package com.hedvig.backoffice.services;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.hedvig.backoffice.services.messages.dto.BotMessageDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Service
public class MessagesFrontendPostprocessor {
  final AmazonS3 amazonS3;
  final String chatS3Bucket;

  public MessagesFrontendPostprocessor(AmazonS3 amazonS3,
      @Value("${hedvig.chat.s3Bucket}") String chatS3Bucket
  ) {
    this.amazonS3 = amazonS3;
    this.chatS3Bucket = chatS3Bucket;
  }

  public void processMessage(BotMessageDTO msg) {
    String bodyType = msg.getBody().path("type").asText();
    if ("file_upload".equals(bodyType)) {
      processFileUpload((ObjectNode) msg.getBody());
    }
  }

  private void processFileUpload(ObjectNode body) {
    String key = body.path("key").asText();
    if (StringUtils.isEmpty(key)) {
      return;
    }

    URL url = amazonS3.generatePresignedUrl(chatS3Bucket, key,
        new Date(Instant.now().plus(30, ChronoUnit.MINUTES).toEpochMilli()), HttpMethod.GET);

    body.set("url", TextNode.valueOf("" + url));
  }

  public URL processFileUrl(String key) {
    return amazonS3.generatePresignedUrl(chatS3Bucket, key,
      new Date(Instant.now().plus(2, ChronoUnit.HOURS).toEpochMilli()), HttpMethod.GET);
  }

  public URL processFileUrl(String key, String s3Bucket) {
    return amazonS3.generatePresignedUrl(s3Bucket, key,
      new Date(Instant.now().plus(2, ChronoUnit.HOURS).toEpochMilli()), HttpMethod.GET);
  }
}
