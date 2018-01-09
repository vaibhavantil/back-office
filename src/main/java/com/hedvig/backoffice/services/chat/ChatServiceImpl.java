package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.chat.dto.MessageDTO;
import com.hedvig.backoffice.services.history.HistoryService;
import com.hedvig.backoffice.services.users.UserNotFoundException;
import com.hedvig.backoffice.services.users.UserService;
import com.hedvig.backoffice.web.dto.UserDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    private final SimpMessagingTemplate template;
    private final HistoryService historyService;
    private final UserService userService;

    public ChatServiceImpl(SimpMessagingTemplate template, HistoryService historyService, UserService userService) {
        this.template = template;
        this.historyService = historyService;
        this.userService = userService;
    }

    @Override
    public void retranslate(String userId, MessageDTO message) throws UserNotFoundException {
        UserDTO user = userService.findUser(userId);
        historyService.appendMessage(user.getId(), message);
        template.convertAndSend("/topic/messages/" + userId, message);
    }

}
