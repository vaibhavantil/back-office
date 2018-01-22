package com.hedvig.backoffice.services.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class BotServiceMessage {

    private JsonNode root;
    private Instant timestamp;
    private Long globalId;

    public BotServiceMessage(String message) throws BotServiceException {
        this(message, false);
    }

    public BotServiceMessage(String message, boolean newMessage) throws BotServiceException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            root = mapper.readValue(message, JsonNode.class);
        } catch (IOException e) {
            throw new BotServiceException(e);
        }

        if (!newMessage) {
            parseGlobalId();
        }

        parseTimestamp();
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Long getGlobalId() {
        return globalId;
    }

    public JsonNode getMessage() {
        return root;
    }

    public void setRoot(JsonNode root) {
        this.root = root;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public void setGlobalId(Long globalId) {
        this.globalId = globalId;
    }

    private void parseTimestamp() throws BotServiceException {
        JsonNode value = Optional.ofNullable(root.get("timestamp"))
                .orElseThrow(() -> new BotServiceException("message must contains timestamp"));

        String timeStr = value.asText();
        if (StringUtils.isBlank(timeStr)) {
            throw new BotServiceException("message must contains timestamp");
        }

        try {
            timestamp = Instant.parse(timeStr);
        } catch (DateTimeParseException e) {
            throw new BotServiceException(e);
        }
    }

    private void parseGlobalId() throws BotServiceException {
        JsonNode value = Optional.ofNullable(root.get("globalId"))
                .orElseThrow(() -> new BotServiceException("message must contains globalId"));

        globalId = value.asLong();
    }

}
