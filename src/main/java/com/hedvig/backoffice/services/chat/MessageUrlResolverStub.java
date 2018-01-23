package com.hedvig.backoffice.services.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hedvig.backoffice.services.messages.data.BotServiceMessage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageUrlResolverStub implements MessageUrlResolver {

    @Override
    public void resolveUrls(BotServiceMessage message) {
        replaceByStub("URL", message.getBody());
        replaceByStub("imageUrl", message.getBody());
    }

    private void replaceByStub(String name, JsonNode body) {
        Optional.ofNullable(body.get(name)).ifPresent(n -> {
            if (n.getNodeType() == JsonNodeType.OBJECT) {
                ObjectNode obj = (ObjectNode) body;
                obj.put(name, "http://78.media.tumblr.com/tumblr_ll313eVnI91qjahcpo1_1280.jpg");
            }
        });
    }
}
