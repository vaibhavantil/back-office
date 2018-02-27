package com.hedvig.backoffice.services.questions;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.questions.dto.QuestionDTO;

import java.util.List;

public interface QuestionService {

    List<QuestionDTO> list();
    List<QuestionDTO> answered();
    List<QuestionDTO> notAnswered();
    void answer(Long id, BotMessage message, Personnel personnel) throws QuestionNotFoundException;
    void answer(String hid, BotMessage message, Personnel personnel);
    void addNewQuestions(List<QuestionDTO> questions);

}
