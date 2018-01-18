package com.hedvig.backoffice.services.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class BotServiceMessage {

    private JsonNode root;
    private Instant timestamp;

    public BotServiceMessage(String message) throws BotServiceException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            root = mapper.readValue(message, JsonNode.class);
        } catch (IOException e) {
            throw new BotServiceException(e);
        }
    }

    public Instant getTimestamp() throws BotServiceException {
        if (timestamp == null) {
            String value = Optional.of(root.get("timestamp"))
                    .map(JsonNode::asText)
                    .orElseThrow(() -> new BotServiceException("message must contains timestamp"));

            try {
                timestamp = Instant.parse(value);
            } catch (DateTimeParseException e) {
                throw new BotServiceException(e);
            }
        }

        return timestamp;
    }

    public JsonNode getMessage() {
        return root;
    }

}
