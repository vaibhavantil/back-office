package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.repository.SubscriptionRepository;
import com.hedvig.backoffice.services.messages.data.PushTokenDTO;
import com.hedvig.backoffice.services.messages.dto.BackOfficeMessage;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class BotServiceStub implements BotService {

    private static Logger logger = LoggerFactory.getLogger(BotServiceStub.class);

    private static final String STUB_MESSAGE_TEMPLATE = "{" +
            "\"globalId\": %s," +
            "\"header\": { " +
            "   \"messageId\": %s," +
            "   \"fromId\": %s" +
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
                "           \"webUrl\": \"http://hedvig.com\"," +
                "           \"selected\": true" +
                "        }" +
                "     ]");


        typesTemplates.put("date_picker", ", \"date\": [2002,8,25,0,0]");

        typesTemplates.put("audio", ", \"URL\": \"http://78.media.tumblr.com/tumblr_ll313eVnI91qjahcpo1_1280.jpg\"");
        typesTemplates.put("photo_upload", ", \"URL\": \"http://78.media.tumblr.com/tumblr_ll313eVnI91qjahcpo1_1280.jpg\"");
        typesTemplates.put("video", ", \"URL\": \"https://media.w3.org/2010/05/sintel/trailer.mp4\"");

        typesTemplates.put("hero", ", \"imageUri\": \"http://78.media.tumblr.com/tumblr_ll313eVnI91qjahcpo1_1280.jpg\"");

        typesTemplates.put("paragraph", "");

        typesTemplates.put("bankid_collect", ", \"referenceId\": \"811228-9874\"");
    }

    private final SubscriptionRepository subscriptionRepository;

    private ConcurrentHashMap<String, List<BotMessage>> messages;
    private ConcurrentHashMap<String, Instant> timestamps;
    private AtomicLong increment;

    @Autowired
    public BotServiceStub(SubscriptionRepository subscriptionRepository) {
        this.messages = new ConcurrentHashMap<>();
        increment = new AtomicLong();
        increment.set(0);

        timestamps = new ConcurrentHashMap<>();

        this.subscriptionRepository = subscriptionRepository;

        logger.info("BOT SERVICE:");
        logger.info("class: " + BotServiceStub.class.getName());
    }

    @Override
    public List<BotMessage> messages(String hid) throws BotServiceException {
        List<BotMessage> current = messages.computeIfAbsent(hid, k -> new ArrayList<>());
        Instant time = new Date().toInstant();
        Instant timestamp = timestamps.computeIfAbsent(hid, k -> new Date().toInstant());

        if (time.minusSeconds(5).isAfter(timestamp)) {
            timestamps.put(hid, new Date().toInstant());
            String type = typeNames.get(current.size() % typeNames.size());

            try {
                current.add(new BotMessage(String.format(STUB_MESSAGE_TEMPLATE,
                        increment.addAndGet(1),
                        current.size(),
                        hid,
                        type,
                        current.size(),
                        typesTemplates.get(type),
                        time.toString())));
            } catch (BotMessageException e) {
                logger.error("error creating message", e);
            }
        }

        return current;
    }

    @Override
    public List<BotMessage> messages(String hid, int count) throws BotServiceException {
        List<BotMessage> all = messages(hid);
        if (all.size() <= count) {
            return all;
        }

        return all.subList(all.size() - count, all.size());
    }

    @Override
    public List<BackOfficeMessage> fetch(Instant timestamp) throws BotServiceException {
        return messages
                .entrySet()
                .stream()
                .flatMap(e -> {
                    String hid = e.getKey();
                    List<BotMessage> messages = e.getValue();
                    return messages
                            .stream()
                            .filter(Objects::nonNull)
                            .filter(m -> m.getTimestamp().isAfter(timestamp))
                            .map(m -> new BackOfficeMessage(hid, m.getMessage()));
                })
                .collect(Collectors.toList());

    }

    @Override
    public void response(String hid, BotMessage message) throws BotServiceException {
        List<BotMessage> msg = messages.computeIfAbsent(hid, k -> new ArrayList<>());
        message.setGlobalId(increment.addAndGet(1));
        message.setMessageId((long) msg.size());

        msg.add(message);
    }

    @Scheduled(fixedDelay = 1000)
    public void addMessage() {
        subscriptionRepository.findActiveSubscriptions().forEach(s -> {
            try {
                messages(s.getHid());
            } catch (BotServiceException e) {
                logger.error("stub bot-service error", e);
            }
        });
    }

	@Override
	public String pushTokenId(String hid) {
		return "";
	}
}
