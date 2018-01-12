package com.hedvig.backoffice.services.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.domain.ChatContext;
import com.hedvig.backoffice.repository.ChatContextRepository;
import com.hedvig.backoffice.services.chat.data.ChatMessage;
import com.hedvig.backoffice.services.chat.data.PayloadChatMessage;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BotServiceImpl implements BotService {

    private static Logger logger = LoggerFactory.getLogger(BotServiceImpl.class);

    private String baseUrl;
    private String messagesUrl;
    private String responseUrl;

    private ChatContextRepository chatContextRepository;

    @Autowired
    private BotServiceImpl(@Value("${botservice.baseUrl}") String baseUrl,
                           @Value("${botservice.urls.messages}") String messagesUrl,
                           @Value("${botservice.urls.response}") String responseUrl,
                           ChatContextRepository chatContextRepository) {

        this.baseUrl = baseUrl;
        this.messagesUrl = messagesUrl;
        this.responseUrl = responseUrl;
        this.chatContextRepository = chatContextRepository;

        logger.info("BOT SERVICE:");
        logger.info("class: " + BotServiceImpl.class.getName());
        logger.info("base: " + baseUrl);
        logger.info("messages: " + messagesUrl);
        logger.info("response: " + responseUrl);
    }

    @Override
    public List<ChatMessage> messages(String hid) throws BotServiceException {
        return messages(baseUrl + messagesUrl, hid);
    }

    @Override
    public List<ChatMessage> messages(String hid, int count) throws BotServiceException {
        return messages(baseUrl + messagesUrl + "/" + count, hid);
    }

    @Override
    public List<ChatMessage> updates(ChatContext chat) throws BotServiceException {
        List<ChatMessage> msgs = messages(baseUrl + messagesUrl + "/5", chat.getHid())
                .stream()
                .filter(msg -> chat.getTimestamp().isBefore(msg.getTimestamp()))
                .collect(Collectors.toList());

        if (msgs.size() > 0) {
            chat.setTimestamp(msgs.get(msgs.size() - 1).getTimestamp());
            chatContextRepository.save(chat);
        }

        return msgs;
    }

    @Override
    public void response(String hid, ChatMessage message) throws BotServiceException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(baseUrl + responseUrl);

        StringEntity entity;
        try {
            entity = new StringEntity(message.getPayload());
        } catch (UnsupportedEncodingException e) {
            throw new BotServiceException(e);
        }

        post.setEntity(entity);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        post.setHeader("hedvig.token", hid);

        doRequest(client, post);
        closeRequest(client);
    }

    private List<ChatMessage> messages(String url, String hid) throws BotServiceException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        get.setHeader("hedvig.token", hid);

        HttpEntity entity = doRequest(client, get);

        ObjectMapper mapper = new ObjectMapper();
        List<ChatMessage> messages = null;

        try {
            String result = IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8);
            if (StringUtils.isNotBlank(result)) {

                JsonNode root = mapper.readValue(result, JsonNode.class);
                Iterable<Map.Entry<String, JsonNode>> iterable = root::fields;

                messages = StreamSupport
                        .stream(iterable.spliterator(), false)
                        .map(e -> new PayloadChatMessage(e.getValue().toString()))
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            closeRequest(client);
            throw new BotServiceException(e);
        }

        closeRequest(client);

        if (messages != null) {
            return messages;
        }

        return new ArrayList<>();
    }

    private HttpEntity doRequest(CloseableHttpClient client, HttpUriRequest request) throws BotServiceException {
        CloseableHttpResponse response;

        try {
            response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();

            if (!(code == 200 || code == 202 || code == 204)) {
                closeRequest(client);
                throw new BotServiceException("bot-service return code " + code);
            }

            return response.getEntity();
        } catch (IOException e) {
            closeRequest(client);
            throw new BotServiceException(e);
        }
    }

    private void closeRequest(CloseableHttpClient client) throws BotServiceException {
        try {
            client.close();
        } catch (IOException e) {
            throw new BotServiceException(e);
        }
    }
}
