package com.hedvig.backoffice.services.autoAnswerSuggestion;

import com.hedvig.backoffice.services.autoAnswerSuggestion.SuggestionDTO.SuggestionDTO;

import java.util.List;


public interface AutoAnswerSuggestionService {

  SuggestionDTO getAnswerSuggestion(String question);

  void autoLabelQuestion(String question, String label, String memberId, List<String> messageId);

}
