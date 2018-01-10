package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.services.messages.data.Message;
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

            Message msg = new Message(String.format("Test message %s", pos.position));
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
