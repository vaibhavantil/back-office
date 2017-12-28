package com.hedvig.backoffice.services.history;

import com.hedvig.backoffice.web.dto.MessageDTO;

import java.util.List;

public interface HistoryService {

    List<MessageDTO> loadHistory(String userId);
    void appendMessage(String userId, MessageDTO message);

}
