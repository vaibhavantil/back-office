package com.hedvig.backoffice.services.autoAnswerSuggestion;

import com.hedvig.backoffice.services.autoAnswerSuggestion.SuggestionDTO.SuggestionDTO;

public interface AutoAnswerSuggestionService {

  SuggestionDTO getAnswerSuggestion(String question);

}
