package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.history.HistoryService;
import com.hedvig.backoffice.services.users.UserNotFoundException;
import com.hedvig.backoffice.services.users.UserService;
import com.hedvig.backoffice.web.dto.MessageDTO;
import com.hedvig.backoffice.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/chat")
public class ChatController {

    private final SimpMessagingTemplate template;
    private final HistoryService historyService;
    private final UserService userService;

    @Autowired
    public ChatController(SimpMessagingTemplate template, HistoryService historyService, UserService userService) {
        this.template = template;
        this.historyService = historyService;
        this.userService = userService;
    }

    @SubscribeMapping("/{id}")
    public void send(@DestinationVariable String id, MessageDTO message) throws UserNotFoundException {
        UserDTO user = userService.findUser(id);
        historyService.appendMessage(user.getId(), message);
        template.convertAndSend("/topic/messages/" + id, message);
    }

}
