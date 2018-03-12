package com.hedvig.backoffice.services.questions;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.questions.dto.QuestionDTO;
import com.hedvig.backoffice.services.questions.dto.QuestionGroupDTO;

import java.util.List;

public interface QuestionService {

    List<QuestionGroupDTO> list();
    List<QuestionGroupDTO> answered();
    List<QuestionGroupDTO> notAnswered();
    void answer(String hid, BotMessage message, Personnel personnel);
    void addNewQuestions(List<BotMessage> questions);

}
