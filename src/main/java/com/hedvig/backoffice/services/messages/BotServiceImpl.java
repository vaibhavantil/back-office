package com.hedvig.backoffice.services.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.services.messages.data.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.data.BotServiceMessage;
import com.hedvig.backoffice.services.messages.data.PushTokenDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.val;

public class BotServiceImpl implements BotService {

    private static Logger logger = LoggerFactory.getLogger(BotServiceImpl.class);

    private String baseUrl;
    private String messagesUrl;
    private String responseUrl;
    private String fetchUrl;
    private BotServiceClient botServiceClient;

    @Autowired
    private BotServiceImpl(@Value("${botservice.baseUrl}") String baseUrl,
                           @Value("${botservice.urls.messages}") String messagesUrl,
                           @Value("${botservice.urls.internal.response}") String responseUrl,
                           @Value("${botservice.urls.internal.fetch}") String fetchUrl,
                           BotServiceClient botServiceClient) {

        this.baseUrl = baseUrl;
        this.messagesUrl = messagesUrl;
        this.responseUrl = responseUrl;
        this.fetchUrl = fetchUrl;
        this.botServiceClient = botServiceClient;

        logger.info("BOT SERVICE:");
        logger.info("class: " + BotServiceImpl.class.getName());
        logger.info("base: " + baseUrl);
        logger.info("messages: " + messagesUrl);
        logger.info("response: " + responseUrl);
    }

    @Override
    public List<BotServiceMessage> messages(String hid) throws BotServiceException {
        return messages(baseUrl + messagesUrl, hid);
    }

    @Override
    public List<BotServiceMessage> messages(String hid, int count) throws BotServiceException {
        return messages(baseUrl + messagesUrl + "/" + count, hid);
    }

    @Override
    public List<BackOfficeMessage> fetch(Instant timestamp) throws BotServiceException {
        RestTemplate template = new RestTemplate();
        try {
            String time = Long.toString(timestamp.toEpochMilli());
            ResponseEntity<BackOfficeMessage[]> messages
                    = template.getForEntity(baseUrl + fetchUrl + "/" + time, BackOfficeMessage[].class);

            return Arrays.asList(messages.getBody());
        } catch (RestClientException e) {
            throw new BotServiceException(e);
        }
    }

    @Override
    public void response(String hid, BotServiceMessage message) throws BotServiceException {
        RestTemplate template = new RestTemplate();

        try {
            HttpEntity<BackOfficeMessage> request
                    = new org.springframework.http.HttpEntity<>(new BackOfficeMessage(hid, message.getMessage()));

            template.postForEntity(baseUrl + responseUrl, request, Void.class);
        } catch (RestClientException e) {
            throw new BotServiceException(e);
        }
    }

    private List<BotServiceMessage> messages(String url, String hid) throws BotServiceException {
        RestTemplate template = new RestTemplate();
        String result;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("hedvig.token", hid);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, request, String.class);
            result = response.getBody();
        } catch (RestClientException e) {
            throw new BotServiceException(e);
        }

        ObjectMapper mapper = new ObjectMapper();
        List<BotServiceMessage> messages = null;

        try {
            if (StringUtils.isNotBlank(result)) {

                JsonNode root = mapper.readValue(result, JsonNode.class);
                Iterable<Map.Entry<String, JsonNode>> iterable = root::fields;

                messages = StreamSupport
                        .stream(iterable.spliterator(), false)
                        .map(e -> {
                            try {
                                return new BotServiceMessage(e.getValue().toString());
                            } catch (BotServiceException ex) {
                                logger.error(ex.getMessage(), ex);
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new BotServiceException(e);
        }

        if (messages != null) {
            return messages;
        }

        return new ArrayList<>();
    }

	@Override
	public String pushTokenId(String hid) {
        val pushTokenDto = botServiceClient.getPushTokenByHid(hid);
        return pushTokenDto.getToken();
	}
}
