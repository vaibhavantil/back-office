package com.hedvig.backoffice.services.messages.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorMessage implements Message {

    private int code;
    private String reason;

    @Override
    public String getPayload() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("error", code);
        root.put("reason", reason);

        return root.toString();
    }
}
