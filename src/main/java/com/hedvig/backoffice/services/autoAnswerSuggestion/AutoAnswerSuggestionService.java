package com.hedvig.backoffice.services.autoAnswerSuggestion;

import com.hedvig.backoffice.services.autoAnswerSuggestion.SuggestionDTO.SuggestionDTO;

public interface AutoAnswerSuggestionService {

  SuggestionDTO getAnswerSuggestion(String question);

  void autoLabelQuestion(String question, String label, String memberId, String messageId);

}
