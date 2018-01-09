package com.hedvig.backoffice.services.history;

import com.hedvig.backoffice.chat.dto.MessageDTO;

import java.util.List;

public interface HistoryService {

    List<MessageDTO> loadHistory(String userId);
    void appendMessage(String userId, MessageDTO message);

}
