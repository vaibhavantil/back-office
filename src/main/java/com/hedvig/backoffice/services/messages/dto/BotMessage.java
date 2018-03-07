package com.hedvig.backoffice.services.messages.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hedvig.backoffice.services.messages.BotMessageException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Getter
public class BotMessage {

    private JsonNode message;

    private Instant timestamp;

    private Long globalId;

    private Long messageId;

    private JsonNode body;

    private JsonNode header;

    private String type;

    private String id;

    private String hid;

    public BotMessage(String message) throws BotMessageException {
        this(message, false);
    }

    public BotMessage(String message, boolean newMessage) throws BotMessageException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.message = mapper.readValue(message, JsonNode.class);
        } catch (IOException e) {
            throw new BotMessageException(e);
        }

        parseFields(newMessage);
    }

    public BotMessage(JsonNode message) throws BotMessageException {
        this(message, false);
    }

    public BotMessage(JsonNode message, boolean newMessage) throws BotMessageException {
        this.message = message;
        parseFields(newMessage);
    }

    public void setGlobalId(Long globalId) {
        this.globalId = globalId;
        ObjectNode root = (ObjectNode) this.message;
        root.put("globalId", globalId);
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
        ObjectNode header = (ObjectNode) this.header;
        header.put("messageId", messageId);
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    private void parseFields(boolean newMessage) throws BotMessageException {
        header = Optional.ofNullable(message.get("header"))
                .orElseThrow(() -> new BotMessageException("message must contains header"));

        body = Optional.ofNullable(message.get("body"))
                .orElseThrow(() -> new BotMessageException("message must contains body"));

        type = Optional.ofNullable(body.get("type"))
                .map(JsonNode::asText)
                .orElseThrow(() -> new BotMessageException("message must contains type"));

        if (!newMessage) {
            globalId = Optional.ofNullable(message.get("globalId"))
                    .orElseThrow(() -> new BotMessageException("message must contains globalId"))
                    .asLong();

            messageId = Optional.ofNullable(header.get("messageId"))
                    .orElseThrow(() -> new BotMessageException("message must contains globalId"))
                    .asLong();

            id = Optional.ofNullable(message.get("id"))
                    .map(JsonNode::asText)
                    .orElse("");
        }

        JsonNode value = Optional.ofNullable(message.get("timestamp"))
                .orElseThrow(() -> new BotMessageException("message must contains timestamp"));

        String timeStr = value.asText();
        if (StringUtils.isBlank(timeStr)) {
            throw new BotMessageException("message must contains timestamp");
        }

        try {
            timestamp = Instant.parse(timeStr);
        } catch (DateTimeParseException e) {
            throw new BotMessageException(e);
        }
    }
}
