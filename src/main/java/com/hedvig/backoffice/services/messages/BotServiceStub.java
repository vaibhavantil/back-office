package com.hedvig.backoffice.services.messages;

import lombok.Value;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BotServiceStub implements BotService {

    private static Logger logger = LoggerFactory.getLogger(BotServiceStub.class);

    private static final String STUB_MESSAGE_TEMPLATE = "{" +
            "\"header\": { " +
            "   \"fromId\": \"%s\"" +
            "}," +
            "\"body\": {" +
            "   \"type\": \"text\"," +
            "   \"text\": \"Test message %s\"" +
            "}," +
            "\"timestamp\":\"%s\"" +
            "}";

    @Value
    private static class MessagePositionStub {
        private Instant time;
        private int position;
    }

    private ConcurrentHashMap<String, List<BotServiceMessage>> messages;
    private ConcurrentHashMap<String, MessagePositionStub> positions;
    private ConcurrentHashMap<String, List<BotServiceMessage>> updateMessages;

    @Autowired
    public BotServiceStub() {
        this.messages = new ConcurrentHashMap<>();
        this.positions = new ConcurrentHashMap<>();
        this.updateMessages = new ConcurrentHashMap<>();

        logger.info("BOT SERVICE:");
        logger.info("class: " + BotServiceStub.class.getName());
    }

    @Override
    public List<BotServiceMessage> messages(String hid) throws BotServiceException {
        return messages.computeIfAbsent(hid, k -> new ArrayList<>());
    }

    @Override
    public List<BotServiceMessage> messages(String hid, int count) throws BotServiceException {
        List<BotServiceMessage> all = messages.computeIfAbsent(hid, k -> new ArrayList<>());
        if (all.size() <= count) {
            return all;
        }

        return all.subList(all.size() - count, all.size());
    }

    @Override
    public List<BotServiceMessage> updates(String hid, Instant timestamp) throws BotServiceException {
        MessagePositionStub pos = positions.computeIfAbsent(hid,
                k -> new MessagePositionStub(Instant.ofEpochMilli(new Date().getTime()), 0));

        List<BotServiceMessage> result = new ArrayList<>();

        Instant current = Instant.ofEpochMilli(new Date().getTime());
        if (current.minusSeconds(RandomUtils.nextInt(3, 7)).isAfter(pos.time)) {
            MessagePositionStub newPos = new MessagePositionStub(Instant.ofEpochMilli(new Date().getTime()), pos.position + 1);
            positions.put(hid, newPos);

            BotServiceMessage msg = new BotServiceMessage(String.format(STUB_MESSAGE_TEMPLATE, hid, pos.position, new Date().toInstant().toString()));
            result.add(msg);
        }

        List<BotServiceMessage> updates = updateMessages.computeIfAbsent(hid, k -> new ArrayList<>());
        if (updates.size() > 0) {
            result.addAll(updates);
            updateMessages.put(hid, new ArrayList<>());
        }

        if (result.size() > 0) {
            List<BotServiceMessage> userMessages = messages.computeIfAbsent(hid, k -> new ArrayList<>());
            userMessages.addAll(result);
        }

        return result;
    }

    @Override
    public void response(String hid, BotServiceMessage message) throws BotServiceException {
        List<BotServiceMessage> msg = updateMessages.computeIfAbsent(hid, k -> new ArrayList<>());
        msg.add(message);
    }
}
