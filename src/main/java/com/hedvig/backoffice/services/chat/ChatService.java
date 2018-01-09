package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.chat.dto.MessageDTO;
import com.hedvig.backoffice.services.users.UserNotFoundException;

public interface ChatService {

    void retranslate(String userId, MessageDTO message) throws UserNotFoundException;

}
