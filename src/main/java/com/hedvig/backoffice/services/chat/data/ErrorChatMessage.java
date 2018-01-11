package com.hedvig.backoffice.services.chat.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.Date;

public class ErrorChatMessage implements ChatMessage {

    private int code;
    private String reason;
    private Instant timestamp;

    public ErrorChatMessage(int code, String reason) {
        this.code = code;
        this.reason = reason;
        this.timestamp = new Date().toInstant();
    }

    @Override
    public String getPayload() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("error", code);
        root.put("reason", reason);
        root.put("timestamp", timestamp.toString());

        return root.toString();
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

}
