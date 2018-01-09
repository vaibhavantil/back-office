package com.hedvig.backoffice.services.history;

import com.hedvig.backoffice.services.users.UserService;
import com.hedvig.backoffice.chat.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HistoryServiceStub implements HistoryService {

    private ConcurrentHashMap<String, List<MessageDTO>> messages;

    private final UserService userService;

    @Autowired
    public HistoryServiceStub(UserService userService) {
        this.userService = userService;
        this.messages = new ConcurrentHashMap<>();
    }

    @Override
    public List<MessageDTO> loadHistory(String userId) {
        return messages.computeIfAbsent(userId, k -> new ArrayList<>());
    }

    @Override
    public void appendMessage(String userId, MessageDTO message) {
        List<MessageDTO> userMessages = messages.computeIfAbsent(userId, k -> new ArrayList<>());
        userMessages.add(message);
    }
}
