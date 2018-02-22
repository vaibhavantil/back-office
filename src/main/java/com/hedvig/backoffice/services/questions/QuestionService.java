package com.hedvig.backoffice.services.questions;

import com.hedvig.backoffice.services.messages.BotServiceException;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.questions.dto.Question;

import java.util.List;

public interface QuestionService {

    List<Question> list();
    boolean answer(String id, BotMessage message) throws BotServiceException;

}
