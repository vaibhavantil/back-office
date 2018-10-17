package com.hedvig.backoffice.services.questions;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.questions.dto.QuestionGroupDTO;
import com.hedvig.backoffice.web.dto.QuestionSortFields;
import com.hedvig.backoffice.web.dto.QuestionSortOrder;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface QuestionService {

  List<QuestionGroupDTO> list();

  List<QuestionGroupDTO> answered(int page, int pageSize, List<QuestionSortOrder> sortOrders);

  List<QuestionGroupDTO> notAnswered(int page, int pageSize, List<QuestionSortOrder> sortOrders);

  QuestionGroupDTO answer(String memberId, String message, Personnel personnel)
      throws QuestionNotFoundException;

  QuestionGroupDTO done(String memberId, Personnel personnel) throws QuestionNotFoundException;

  void addNewQuestions(List<BotMessage> questions);
}
