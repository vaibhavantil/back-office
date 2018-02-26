package com.hedvig.backoffice.services.questions;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.questions.dto.QuestionDTO;

import java.util.List;

public interface QuestionService {

    List<QuestionDTO> list();
    boolean answer(Long id, BotMessage message, Personnel personnel) throws QuestionNotFoundException;
    void save(List<QuestionDTO> questions);

}
