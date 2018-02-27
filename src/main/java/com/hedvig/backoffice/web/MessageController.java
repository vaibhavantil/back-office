package com.hedvig.backoffice.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.services.chat.ChatService;
import com.hedvig.backoffice.services.messages.BotMessageException;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceException;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final BotService botService;
    private final ChatService chatService;

    @Autowired
    public MessageController(BotService botService, ChatService chatService) {
        this.botService = botService;
        this.chatService = chatService;
    }

    @GetMapping("/{hid}")
    public List<JsonNode> messages(@PathVariable String hid) throws BotServiceException {
        return botService.messages(hid).stream().map(BotMessage::getMessage).collect(Collectors.toList());
    }

    @GetMapping("/{hid}/{count}")
    public List<JsonNode> messages(@PathVariable String hid, @PathVariable int count) throws BotServiceException {
        return botService.messages(hid, count).stream().map(BotMessage::getMessage).collect(Collectors.toList());
    }

    @PostMapping("/response/{hid}")
    public ResponseEntity<?> response(@PathVariable String hid,
                                      @RequestBody JsonNode body,
                                      @AuthenticationPrincipal String principal)
            throws BotMessageException {
        chatService.append(hid, new BotMessage(body, true), principal);
        return ResponseEntity.noContent().build();
    }

}
