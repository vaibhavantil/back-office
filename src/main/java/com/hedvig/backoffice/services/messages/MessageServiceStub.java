package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.services.messages.data.Message;
import com.hedvig.backoffice.services.messages.data.PayloadMessage;
import lombok.Value;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageServiceStub implements MessageService {

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

    private ConcurrentHashMap<String, List<Message>> messages;
    private ConcurrentHashMap<String, MessagePositionStub> positions;

    @Autowired
    public MessageServiceStub() {
        this.messages = new ConcurrentHashMap<>();
        this.positions = new ConcurrentHashMap<>();
    }

    @Override
    public List<Message> messages(String hid) {
        return messages.computeIfAbsent(hid, k -> new ArrayList<>());
    }

    @Override
    public List<Message> messages(String hid, int count) {
        List<Message> all = messages.computeIfAbsent(hid, k -> new ArrayList<>());
        if (all.size() <= count) {
            return all;
        }

        return all.subList(all.size() - count, all.size());
    }

    @Override
    public List<Message> updates(String hid) {
        MessagePositionStub pos = positions.computeIfAbsent(hid,
                k -> new MessagePositionStub(Instant.ofEpochMilli(new Date().getTime()), 0));

        Instant current = Instant.ofEpochMilli(new Date().getTime());
        if (current.minusSeconds(RandomUtils.nextInt(3, 7)).isAfter(pos.time)) {
            MessagePositionStub newPos = new MessagePositionStub(Instant.ofEpochMilli(new Date().getTime()), pos.position + 1);
            positions.put(hid, newPos);

            Message msg = new PayloadMessage(String.format(STUB_MESSAGE_TEMPLATE, pos.position, pos.position));
            return Arrays.asList(msg);
        }

        return new ArrayList<>();
    }

    @Override
    public void response(String hid, Message message) {
        List<Message> userMessages = messages.computeIfAbsent(hid, k -> new ArrayList<>());
        userMessages.add(message);
    }
}
