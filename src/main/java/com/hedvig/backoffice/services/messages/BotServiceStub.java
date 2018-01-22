package com.hedvig.backoffice.services.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.*;
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
            "   \"type\": \"%s\"," +
            "   \"text\": \"Test message %s\"" +
            "   %s" +
            "}," +
            "\"timestamp\":\"%s\"" +
            "}";

    private static List<String> typeNames = Arrays.asList("text", "number", "single_select", "multiple_select",
            "date_picker", "audio", "photo_upload", "video", "hero", "paragraph", "bankid_collect");
    private static Map<String, String> typesTemplates = new HashMap<>();

    static {
        typesTemplates.put("text", "");
        typesTemplates.put("number", "");

        typesTemplates.put("single_select", ",  \"choices\":[" +
                "        {" +
                "           \"type\": \"selection\"," +
                "           \"text\":\"Jag vill ha en ny\"," +
                "           \"selected\": false" +
                "        },{" +
                "           \"type\": \"link\"," +
                "           \"text\":\"I want to see my assets\"," +
                "           \"view\": \"AssetTracker\"," +
                "           \"appUrl\": \"bankid://\"," +
                "           \"webUrl\": \"http://hedvig.com\"," +
                "           \"selected\": true" +
                "        }" +
                "     ]");


        typesTemplates.put("multiple_select", ",  \"choices\":[" +
                "        {" +
                "           \"type\": \"selection\"," +
                "           \"text\":\"Jag vill ha en ny\"," +
                "           \"selected\": false" +
                "        },{" +
                "           \"type\": \"link\"," +
                "           \"text\":\"I want to see my assets\"," +
                "           \"view\": \"AssetTracker\"," +
                "           \"appUrl\": \"bankid://\"," +
                "           \"webUrl\": \"http://hedvig.com\"," +
                "           \"selected\": true" +
                "        }" +
                "     ]");


        typesTemplates.put("date_picker", ", \"date\": [2002,8,25,0,0]");

        typesTemplates.put("audio", ", \"URL\": \"http://78.media.tumblr.com/tumblr_ll313eVnI91qjahcpo1_1280.jpg\"");
        typesTemplates.put("photo_upload", ", \"URL\": \"http://78.media.tumblr.com/tumblr_ll313eVnI91qjahcpo1_1280.jpg\"");
        typesTemplates.put("video", ", \"URL\": \"http://78.media.tumblr.com/tumblr_ll313eVnI91qjahcpo1_1280.jpg\"");

        typesTemplates.put("hero", ", \"imageUri\": \"http://78.media.tumblr.com/tumblr_ll313eVnI91qjahcpo1_1280.jpg\"");

        typesTemplates.put("paragraph", "");

        typesTemplates.put("bankid_collect", ", \"referenceId\": \"811228-9874\"");
    }

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
            String type = typeNames.get(current.size() % typeNames.size());

            current.add(new BotServiceMessage(String.format(STUB_MESSAGE_TEMPLATE,
                    increment.addAndGet(1),
                    current.size(),
                    hid,
                    type,
                    current.size(),
                    typesTemplates.get(type),
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
