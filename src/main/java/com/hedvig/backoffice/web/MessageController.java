package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceException;
import com.hedvig.backoffice.services.chat.data.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final BotService botService;

    @Autowired
    public MessageController(BotService botService) {
        this.botService = botService;
    }

    @GetMapping("/{hid}")
    public List<ChatMessage> messages(@PathVariable String hid) throws BotServiceException {
        return botService.messages(hid);
    }

    @GetMapping("/{hid}/{count}")
    public List<ChatMessage> messages(@PathVariable String hid, @PathVariable int count) throws BotServiceException {
        return botService.messages(hid, count);
    }

}
