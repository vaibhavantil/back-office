package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.domain.ChatContext;
import com.hedvig.backoffice.services.chat.data.ChatMessage;
import com.hedvig.backoffice.services.chat.data.PayloadChatMessage;
import lombok.Value;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BotServiceStub implements BotService {

    private static final String STUB_MESSAGE_TEMPLATE = "{" +
            "'globalId': %s, " +
            "'id': 'message.onboardingstart'," +
            "'header': { " +
            "   'messageId': 1," +
            "   'fromId': 1," +
            "   'responsePath': '/response', " +
            "   'timeStamp': 1515499154030," +
            "   'loadingIndicator': 'loader'," +
            "   'avatarName': null, " +
            "   'pollingInterval': 625," +
            "   'editAllowed': false" +
            "}," +
            "'body': {" +
            "   'type': 'paragraph'," +
            "   'id': 1," +
            "   'text': 'Test message %s', " +
            "   'imageURL': null," +
            "   'imageWidth': null," +
            "   'imageHeight': null" +
            "}," +
            "'timestamp':'2018-01-09T11:59:14.030Z'" +
            "}";

    @Value
    private static class MessagePositionStub {
        private Instant time;
        private int position;
    }

    private ConcurrentHashMap<String, List<ChatMessage>> messages;
    private ConcurrentHashMap<String, MessagePositionStub> positions;
    private ConcurrentHashMap<String, List<ChatMessage>> updateMessages;

    @Autowired
    public BotServiceStub() {
        this.messages = new ConcurrentHashMap<>();
        this.positions = new ConcurrentHashMap<>();
        this.updateMessages = new ConcurrentHashMap<>();
    }

    @Override
    public List<ChatMessage> messages(String hid) throws BotServiceException {
        return messages.computeIfAbsent(hid, k -> new ArrayList<>());
    }

    @Override
    public List<ChatMessage> messages(String hid, int count) throws BotServiceException {
        List<ChatMessage> all = messages.computeIfAbsent(hid, k -> new ArrayList<>());
        if (all.size() <= count) {
            return all;
        }

        return all.subList(all.size() - count, all.size());
    }

    @Override
    public List<ChatMessage> updates(ChatContext chat) throws BotServiceException {
        MessagePositionStub pos = positions.computeIfAbsent(chat.getHid(),
                k -> new MessagePositionStub(Instant.ofEpochMilli(new Date().getTime()), 0));

        List<ChatMessage> result = new ArrayList<>();

        Instant current = Instant.ofEpochMilli(new Date().getTime());
        if (current.minusSeconds(RandomUtils.nextInt(3, 7)).isAfter(pos.time)) {
            MessagePositionStub newPos = new MessagePositionStub(Instant.ofEpochMilli(new Date().getTime()), pos.position + 1);
            positions.put(chat.getHid(), newPos);

            ChatMessage msg = new PayloadChatMessage(String.format(STUB_MESSAGE_TEMPLATE, pos.position, pos.position));
            result.add(msg);
        }

        List<ChatMessage> updates = updateMessages.computeIfAbsent(chat.getHid(), k -> new ArrayList<>());
        if (updates.size() > 0) {
            result.addAll(updates);
            updateMessages.put(chat.getHid(), new ArrayList<>());
        }

        if (result.size() > 0) {
            List<ChatMessage> userMessages = messages.computeIfAbsent(chat.getHid(), k -> new ArrayList<>());
            userMessages.addAll(result);
        }

        return result;
    }

    @Override
    public void response(String hid, ChatMessage message) throws BotServiceException {
        List<ChatMessage> msg = updateMessages.computeIfAbsent(hid, k -> new ArrayList<>());
        msg.add(message);
    }
}
