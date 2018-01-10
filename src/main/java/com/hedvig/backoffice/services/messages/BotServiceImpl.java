package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.services.messages.data.Message;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class BotServiceImpl implements BotService {

    private String baseUrl;
    private String messagesUrl;
    private String responseUrl;

    @Autowired
    private BotServiceImpl(@Value("${botservice.baseUrl}") String baseUrl,
                           @Value("${botservice.urls.messages}") String messagesUrl,
                           @Value("${botservice.urls.response}") String responseUrl) {

        this.baseUrl = baseUrl;
        this.messagesUrl = messagesUrl;
        this.responseUrl = responseUrl;
    }

    @Override
    public List<Message> messages(String hid) {



        return new ArrayList<>();
    }

    @Override
    public List<Message> messages(String hid, int count) {
        return new ArrayList<>();
    }

    @Override
    public List<Message> updates(String hid) {
        return new ArrayList<>();
    }

    @Override
    public void response(String hid, Message message) throws BotServiceException {
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

        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);
            int code = response.getStatusLine().getStatusCode();
            client.close();

            if (!(code == 200 || code == 202 || code == 204)) {
                throw new BotServiceException("bot-service return code " + code);
            }

        } catch (IOException e) {
            throw new BotServiceException(e);
        }
    }
}
