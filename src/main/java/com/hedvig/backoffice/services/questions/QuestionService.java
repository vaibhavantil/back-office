package com.hedvig.backoffice.services.questions;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.domain.QuestionGroup;
import com.hedvig.backoffice.services.messages.dto.BotMessageDTO;
import com.hedvig.backoffice.services.questions.dto.QuestionGroupDTO;
import java.util.List;

public interface QuestionService {

  List<QuestionGroupDTO> list();

  List<QuestionGroupDTO> notAnswered();

  QuestionGroupDTO answer(String memberId, String message, Personnel personnel)
      throws QuestionNotFoundException;

  QuestionGroupDTO done(String memberId, Personnel personnel) throws QuestionNotFoundException;

  void addNewQuestions(List<BotMessageDTO> questions);
}
