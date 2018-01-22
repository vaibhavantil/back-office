package com.hedvig.backoffice.services.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class BotServiceStub implements BotService {

    private static Logger logger = LoggerFactory.getLogger(BotServiceStub.class);

    private static final String STUB_MESSAGE_TEMPLATE = "{" +
            "\"globalId\": %s," +
            "\"header\": { " +
            "   \"messageId\": %s," +
            "   \"fromId\": \"%s\"" +
            "}," +
            "\"body\": {" +
            "   \"type\": \"text\"," +
            "   \"text\": \"Test message %s\"" +
            "}," +
            "\"timestamp\":\"%s\"" +
            "}";

    private ConcurrentHashMap<String, List<BotServiceMessage>> messages;
    private Instant timestamp;
    private AtomicLong increment;

    @Autowired
    public BotServiceStub() {
        this.messages = new ConcurrentHashMap<>();
        increment = new AtomicLong();
        increment.set(0);

        timestamp = new Date().toInstant();

        logger.info("BOT SERVICE:");
        logger.info("class: " + BotServiceStub.class.getName());
    }

    @Override
    public List<BotServiceMessage> messages(String hid) throws BotServiceException {
        List<BotServiceMessage> current = messages.computeIfAbsent(hid, k -> new ArrayList<>());
        Instant time = new Date().toInstant();
        if (time.minusSeconds(5).isAfter(timestamp)) {
            timestamp = new Date().toInstant();
            current.add(new BotServiceMessage(String.format(STUB_MESSAGE_TEMPLATE,
                    increment.addAndGet(1),
                    current.size(),
                    hid,
                    current.size(),
                    timestamp.toString())));
        }

        return current;
    }

    @Override
    public List<BotServiceMessage> messages(String hid, int count) throws BotServiceException {
        List<BotServiceMessage> all = messages(hid);
        if (all.size() <= count) {
            return all;
        }

        return all.subList(all.size() - count, all.size());
    }

    @Override
    public void response(String hid, BotServiceMessage message) throws BotServiceException {
        List<BotServiceMessage> msg = messages.computeIfAbsent(hid, k -> new ArrayList<>());
        message.setGlobalId(increment.addAndGet(1));

        msg.add(message);
    }
}
