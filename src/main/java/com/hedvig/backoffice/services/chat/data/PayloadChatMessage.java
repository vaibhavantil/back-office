package com.hedvig.backoffice.services.chat.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class PayloadChatMessage implements ChatMessage {

    private static Logger logger = LoggerFactory.getLogger(PayloadChatMessage.class);

    private String payload;
    private JsonNode node;
    private Instant timestamp;

    public PayloadChatMessage(String payload) {
        this.payload = payload;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public Instant getTimestamp() {
        if (timestamp == null) {
            try {
                timestamp = Instant.parse(getRootNode().get("timestamp").asText());
            } catch (IOException e) {
                logger.error("can't parse chat message", e);
                timestamp = new Date().toInstant();
            }
        }

        return timestamp;
    }

    private JsonNode getRootNode() throws IOException {
        if (node == null) {
            ObjectMapper mapper = new ObjectMapper();
            node = mapper.readValue(payload, JsonNode.class);
        }

        return node;
    }

}
