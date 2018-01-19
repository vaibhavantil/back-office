package com.hedvig.backoffice.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceException;
import com.hedvig.backoffice.services.messages.BotServiceMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final BotService botService;

    @Autowired
    public MessageController(BotService botService) {
        this.botService = botService;
    }

    @GetMapping("/{hid}")
    public List<JsonNode> messages(@PathVariable String hid) throws BotServiceException {
        return botService.messages(hid).stream().map(BotServiceMessage::getMessage).collect(Collectors.toList());
    }

    @GetMapping("/{hid}/{count}")
    public List<JsonNode> messages(@PathVariable String hid, @PathVariable int count) throws BotServiceException {
        return botService.messages(hid, count).stream().map(BotServiceMessage::getMessage).collect(Collectors.toList());
    }

    @PostMapping("/response/{hid}")
    public ResponseEntity<?> response(@PathVariable String hid, @RequestBody String body) throws BotServiceException {
        botService.response(hid, new BotServiceMessage(body));
        return ResponseEntity.noContent().build();
    }

}
