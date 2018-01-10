package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.messages.MessageService;
import com.hedvig.backoffice.services.messages.data.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{hid}")
    public List<Message> messages(@PathVariable String hid) {
        return messageService.messages(hid);
    }

    @GetMapping("/{hid}/{count}")
    public List<Message> messages(@PathVariable String hid, @PathVariable int count) {
        return messageService.messages(hid, count);
    }

}
