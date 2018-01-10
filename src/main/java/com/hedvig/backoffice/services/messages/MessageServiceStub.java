package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.services.messages.data.Message;
import lombok.Value;
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

    private static List<String> TEST_MESSAGES = Arrays.asList(
            "Test Message 1",
            "Test Message 2",
            "Test Message 3",
            "Test Message 4",
            "Test Message 5"
    );

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
        if (current.minusSeconds(5).isAfter(pos.time)) {
            if (pos.position < TEST_MESSAGES.size()) {
                MessagePositionStub newPos = new MessagePositionStub(Instant.ofEpochMilli(new Date().getTime()), pos.position + 1);
                positions.put(hid, newPos);

                Message msg = new Message(TEST_MESSAGES.get(pos.position));
                return Arrays.asList(msg);
            }
        }

        return new ArrayList<>();
    }

    @Override
    public void response(String hid, Message message) {
        List<Message> userMessages = messages.computeIfAbsent(hid, k -> new ArrayList<>());
        userMessages.add(message);
    }
}
